package com.fruitshop.constant;

import java.util.HashMap;
import java.util.Map;

public class VNPayreturnCode {
	public static final Map<String, String> CODE_MAP = new HashMap<>();

    static {
        CODE_MAP.put("00", "Giao dịch thành công");
        CODE_MAP.put("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).");
        CODE_MAP.put("09", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.");
        CODE_MAP.put("10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần");
        CODE_MAP.put("11", "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.");
        CODE_MAP.put("12", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.");
        CODE_MAP.put("13", "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.");
        CODE_MAP.put("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch");
        CODE_MAP.put("51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
        CODE_MAP.put("65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.");
        CODE_MAP.put("75", "Ngân hàng thanh toán đang bảo trì.");
        CODE_MAP.put("79", "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch.");
    }

    public static String getMessage(String code) {
        return CODE_MAP.getOrDefault(code, "Mã giao dịch không tồn tại.");
    }

}
