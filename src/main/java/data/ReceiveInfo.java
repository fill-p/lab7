package data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveInfo {

    private long packageId;
    private String receiveDate;
    private boolean isReceive;
}
