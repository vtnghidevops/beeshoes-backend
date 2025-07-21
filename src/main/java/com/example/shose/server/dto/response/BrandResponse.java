package com.example.shose.server.dto.response;

import com.example.shose.server.dto.response.base.BaseResponse;
import com.example.shose.server.entity.Brand;
import org.springframework.data.rest.core.config.Projection;

/**
 * @author Nguyễn Vinh
 */
@Projection(types = Brand.class)
public interface BrandResponse extends BaseResponse {
}
