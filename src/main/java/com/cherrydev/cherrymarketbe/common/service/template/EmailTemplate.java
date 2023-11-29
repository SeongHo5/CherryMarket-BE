package com.cherrydev.cherrymarketbe.common.service.template;

public class EmailTemplate {

    private EmailTemplate() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static final String VERIFICATION_TITTLE = "[MyVelog] 회원가입 본인 인증 메일입니다.";

    public static String createVerificationMessage(final String verificationCode) {
        return "<div style=\"width: 100%; height: 100%; display: flex; justify-content: center; align-items: center;\">\n" +
                "    <div style=\"width: 500px; height: 500px; display: flex; flex-direction: column; justify-content: center; align-items: center;\">\n" +
                "        <div style=\"font-size: 30px; font-weight: bold; margin-bottom: 20px;\">MyVelog 회원가입 인증 메일입니다.</div>\n" +
                "        <div style=\"font-size: 20px; margin-bottom: 20px;\">아래의 인증 코드를 입력해주세요.</div>\n" +
                "        <div style=\"font-size: 30px; font-weight: bold; margin-bottom: 20px;\">" + verificationCode + "</div>\n" +
                "        <div style=\"font-size: 20px; margin-bottom: 20px;\">감사합니다.</div>\n" +
                "    </div>\n" +
                "</div>";
    }

    public static final String PW_RESET_TITTLE = "[MyVelog] 비밀번호 재설정 메일입니다.";

    public static String createPasswordResetMessage(final String verificationCode) {
        return "<div style=\"width: 100%; height: 100%; display: flex; justify-content: center; align-items: center;\">\n" +
                "    <div style=\"width: 500px; height: 500px; display: flex; flex-direction: column; justify-content: center; align-items: center;\">\n" +
                "        <div style=\"font-size: 30px; font-weight: bold; margin-bottom: 20px;\">MyVelog 비밀번호 재설정 메일입니다.</div>\n" +
                "        <div style=\"font-size: 20px; margin-bottom: 20px;\">아래의 인증 코드를 입력해주세요.</div>\n" +
                "        <div style=\"font-size: 30px; font-weight: bold; margin-bottom: 20px;\">" + verificationCode + "</div>\n" +
                "        <div style=\"font-size: 20px; margin-bottom: 20px;\">감사합니다.</div>\n" +
                "    </div>\n" +
                "</div>";
    }

}
