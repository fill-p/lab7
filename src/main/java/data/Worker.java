package data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Worker {

    private long id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
}
