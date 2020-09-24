package please.help;

import please.help.commands.Command;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Scanner;

public class Client {

    private final String ip;
    private final int port;
    private final LinkedList<Command> commandsToSend = new LinkedList<>();
    private int collectionHash = 0;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void addCommand(Command command){
        commandsToSend.add(command);
    }

    public void printSavedCommands(){
        commandsToSend.forEach(p -> System.out.println(p.getCommandName()));
    }

    public void sendCommands(){
        while (commandsToSend.size() != 0) {
            String information = "Вы можете прервать процесс установления соединения с сервером и " +
                    "продолжить вводить команды.\n"
                    + "Неотправленные команды сохраняются.\n" +
                    "Вывести сохраненые неотправленные команды: 'saved_commands'\n" +
                    "Отправить все сохраненные команды: 'send_commands'";
            System.out.println(commandsToSend.peek().getCommandName());
            ServerPackage feedback = sendCommand(commandsToSend.peek(), information);
            if (feedback != null){
                System.out.println(feedback.getServerFeedback());
                commandsToSend.poll();
            }
            else break;
        }
    }

    public ServerPackage sendCommand(Command command, String informationOnConnectingProblem){
        while (true) {
            try {
                SocketChannel socket = SocketChannel.open();
                socket.configureBlocking(false);
                Selector selector = Selector.open();
                socket.register(selector, SelectionKey.OP_CONNECT);

                try {
                    socket.connect(new InetSocketAddress(ip, port));
                    if (!(selector.select(3 * 1000) == 1 && socket.finishConnect())) throw new IOException();
                }
                catch (Exception e) {
                    System.out.println("Не удается установить соединение с сервером.\n"
                            + informationOnConnectingProblem + "\n"
                            + "Прервать процесс установления соединения с сервером? (y/n)");
                    Scanner scan = new Scanner(System.in);
                    while (true) {
                        String[] input = scan.nextLine().trim().split("\\s+");
                        if (input.length == 1) {
                            if (input[0].equals("y")) {
                                return null;
                            }
                            if (input[0].equals("n")) {
                                socket.close();
                                selector.close();
                                break;
                            }
                        }
                        System.out.println("Некорректный ввод. Повторите попытку.");
                    }
                    continue;
                }

                try{
                    //System.out.println("Соединение с сервером установлено");

                    //System.out.println("Принимаю хеш коллекции");
                    ByteBuffer buffer = ByteBuffer.allocate(50000);
                    while (buffer.position() < 4) socket.read(buffer);
                    buffer.flip();
                    int packageLength = buffer.getInt();
                    buffer.compact();
                    while (buffer.position() != packageLength) {
                        socket.read(buffer);
                    }
                    ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                    int inputtedHash = ((ServerPackage) input.readObject()).getCollectionHash();
                    //System.out.println("Хеш получен");

                    if (collectionHash == 0) collectionHash = inputtedHash;
                    else if (collectionHash != inputtedHash) {
                        System.out.println("Коллекция была изменена с момента последнего подключения к серверу.\n" +
                                "Все равно отправить команды? (y/n) В случае отмены все неотправленные команды " +
                                "будут удалены.");
                        Scanner scan = new Scanner(System.in);
                        while (true) {
                            String[] consoleInput = scan.nextLine().trim().split("\\s+");
                            if (consoleInput.length == 1) {
                                if (consoleInput[0].equals("y")) break;
                                if (consoleInput[0].equals("n")) {
                                    commandsToSend.clear();
                                    input.close();
                                    socket.close();
                                    return null;
                                }
                            }
                            System.out.println("Некорректный ввод. Повторите попытку.");
                        }
                    }

                    //System.out.println("Отправляю команду");
                    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                    ObjectOutputStream output = new ObjectOutputStream(byteOutput);
                    output.writeObject(command);
                    output.flush();
                    ByteBuffer bufferToSend = ByteBuffer.wrap(byteOutput.toByteArray());

                    byteOutput.reset();
                    DataOutputStream dos = new DataOutputStream(byteOutput);
                    dos.writeInt(bufferToSend.capacity());
                    dos.flush();
                    ByteBuffer commandLen = ByteBuffer.wrap(byteOutput.toByteArray());
                    dos.close();
                    while (commandLen.hasRemaining()) socket.write(commandLen);
                    while (bufferToSend.hasRemaining()) socket.write(bufferToSend);
                    //System.out.println("Команда отправлена");

                    //System.out.println("Принимаю ответ от сервера");
                    buffer.clear();
                    while (buffer.position() < 4) {
                        socket.read(buffer);
                    }
                    buffer.flip();
                    packageLength = buffer.getInt();
                    buffer.compact();

                    while (buffer.position() != packageLength) socket.read(buffer);
                    ServerPackage feedback = (ServerPackage) input.readObject();
                    collectionHash = feedback.getCollectionHash();

                    output.close();
                    input.close();
                    socket.close();
                    selector.close();
                    //System.out.println("Связь с сервером разорвана");
                    return feedback;
                }
                catch (Exception e){
                    System.out.println("Во врнемя передачи команды серверу произошла ошибка.");
                    return null;
                }
            }
            catch (Exception e){
                System.out.println("Произощла ошибка во время инициализации клиента.");
                return null;
            }
        }
    }
}
