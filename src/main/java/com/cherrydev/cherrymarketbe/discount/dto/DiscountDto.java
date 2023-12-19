package com.cherrydev.cherrymarketbe.discount.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

import java.sql.Date;

@Value
@Builder
public class DiscountDto {

    Long discountId;

    @Pattern(regexp = "^[^\\s]+$", message = "할인코드에서 공백은 사용할 수 없습니다")
    String discountCode;

    String discountType;

    @Min(value = 0, message = "할인율은 0 미만의 값을 입력할 수 없습니다.")
    @Max(value = 100, message = "할인율은 100을 초과하는 값을 입력할 수 없습니다.")
    int discountRate;

    Date discountBeginDate;

    Date discountEndDate;

    Date createDate;

    Date updateDate;

}
