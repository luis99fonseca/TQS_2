package tqs.justlikehome.dtos;

public class BookMarkDTO {
    long userId;
    long houseId;

    public BookMarkDTO(long userId, long houseId) {
        this.userId = userId;
        this.houseId = houseId;
    }

    public long getUserId() {
        return userId;
    }

    public long getHouseId() {
        return houseId;
    }
}
