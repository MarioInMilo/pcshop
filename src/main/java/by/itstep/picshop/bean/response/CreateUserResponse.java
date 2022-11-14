package by.itstep.picshop.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponse extends BaseResponse implements Serializable {
    private String test;
    private Long userId;
    private String userName;

    public CreateUserResponse(Integer code, String message) {
        super(code, message);

    }
}
