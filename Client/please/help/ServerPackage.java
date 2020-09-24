package please.help;

import please.help.organizationBuilding.Organization;

import java.io.Serializable;

public class ServerPackage implements Serializable {

    private static final long serialVersionUID = 20200916L;
    private final int collectionHash;
    private final String serverFeedback;
    private final Organization orgForUpdate;

    public ServerPackage(int collectionHash, String serverFeedback, Organization orgForUpdate){
        this.collectionHash = collectionHash;
        this.serverFeedback = serverFeedback;
        this.orgForUpdate = orgForUpdate;
    }

    public int getCollectionHash() {
        return collectionHash;
    }

    public String getServerFeedback() {
        return serverFeedback;
    }

    public Organization getOrgForUpdate() {
        return orgForUpdate;
    }
}
