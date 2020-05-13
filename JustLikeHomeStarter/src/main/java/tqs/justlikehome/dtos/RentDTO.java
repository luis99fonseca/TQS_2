package tqs.justlikehome.dtos;

public class RentDTO {
    private long houseID;
    private long userID;
    private String startDate;
    private String endDate;

    public RentDTO(long houseID, long userID, String startDate, String endDate) {
        this.houseID = houseID;
        this.userID = userID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getHouseID() {
        return houseID;
    }

    public long getUserID() {
        return userID;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
