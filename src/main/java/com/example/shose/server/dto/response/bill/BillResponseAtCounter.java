package com.example.shose.server.dto.response.bill;

import com.example.shose.server.entity.Bill;
import com.example.shose.server.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * @author thangdt
 */
@Projection(types = {Bill.class, User.class})
public interface BillResponseAtCounter {

    @Value("#{target.stt}")
    String getStt();

    @Value("#{target.id}")
    String getId();

    @Value("#{target.code}")
    String getCode();

    @Value("#{target.created_date}")
    long getCreatedDate();

    @Value("#{target.userName}")
    String getUserName();

    @Value("#{target.status_bill}")
    String getStatusBill();

    @Value("#{target.total_money}")
    BigDecimal getTotalMoney();

    @Value("#{target.item_discount}")
    BigDecimal getItemDiscount();

    @Value("#{target.quantity}")
    int getQuantity();
}
