package data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PackageInfo {

    private int id;
    @NonNull
    private String sender;
    @NonNull
    private String receiver;
}
