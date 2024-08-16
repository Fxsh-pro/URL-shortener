package faang.school.urlshortenerservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDto {
    @URL(message = "Invalid URL format")
    @NotNull
    private String oldUrl;

    @URL(message = "Invalid URL format")
    @NotNull
    private String newUrl;
}
