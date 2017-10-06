package be.deschutter.scimitar.intel;

public interface IntelService {
    Intel findBy(int x, int y, int z);

    Intel changeNick(int x, int y, int z, String newNickname);

    Intel changeAlliance(int x, int y, int z, String allianceName);

    Intel addNick(int x, int y, int z, String newNickname);
}
