package please.help;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import please.help.commands.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.LinkedList;

public class Server {

    public static final Logger logger = (Logger) LoggerFactory.getLogger(Server.class.getName());
    private final int port;

    public Server(int port){
        this.port = port;
    }

    public void startReceiving(CollectionManager manager) throws Exception{

        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        LinkedList<String[]> inputList = new LinkedList<>();

        logger.info("Попытка инициализации сервера: port: {}", port);
        ServerSocket server = new ServerSocket(port);
        server.setSoTimeout(1000);
        System.out.println("Сервер готов к работе.\n"
                + "Вы все еще можете вводить команды для управления коллекцией.\n"
                + "Вывести справку по доступным командам: 'help'.\n-----");
        logger.info("Сервер начал работу:  port: {}", server.getLocalPort());

        while (manager.getManagerMode() != Mode.EXIT) {
            try (Socket client = server.accept();
                 DataInputStream input  = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                 DataOutputStream output = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()))){
                logger.info("Установлено соединение с клиентом: ip: {}, port: {}", client.getInetAddress().getAddress()
                        , client.getPort());

                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
                objectOutput.writeObject(new ServerPackage(manager.collection.hashCode()
                        , null, null));
                objectOutput.flush();
                logger.info("Начало отправления хеша коллекции: {} bytes", byteOutput.toByteArray().length);
                output.writeInt(byteOutput.toByteArray().length);
                output.write(byteOutput.toByteArray());
                output.flush();
                logger.info("Хеш коллекции отправлен");

                logger.info("Начало приема команды от клиента");
                int commandLen = input.readInt();
                byte[] buffer = new byte[50000];
                int counter = 0;
                while (counter != commandLen) counter += input.read(buffer);
                ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(buffer));
                Command commandFromClient = (Command) objectInput.readObject();
                logger.info("Получена команда {} : {} bytes", commandFromClient.getCommandName()
                        , commandLen);

                ServerPackage feedback = commandFromClient.execute(manager);
                manager.addCommandToHistory(commandFromClient);
                logger.info("Команда выполнена");
                objectOutput.writeObject(feedback);
                objectOutput.flush();
                logger.info("Начало отправки ответа клиенту : {} bytes", byteOutput.toByteArray().length);
                output.writeInt(byteOutput.toByteArray().length);
                output.write(byteOutput.toByteArray());
                output.flush();
                logger.info("Ответ отправлен");

                objectInput.close();
                objectOutput.close();
                logger.info("Соединение с сервером разорвано");
            }
            catch (SocketTimeoutException ignore){}

            catch (Exception e) {
                logger.warn("Во время взаимодествия с клиентом произошла ошибка", e);
            }
            finally {
                if (consoleInput.ready()){
                    inputList.add(consoleInput.readLine().trim().split("\\s+"));
                    logger.info("С консоли введена строка : {}", Arrays.toString(inputList.peek()));
                    manager.executeCommand(inputList);
                }
            }
        }
        logger.info("Сервер прекращает работу по команде с консоли");
    }
}
