package by.itstep.picshop.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private Integer code;
    private String message;
    private Object errorDetails;

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
