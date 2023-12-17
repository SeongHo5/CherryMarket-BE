package com.cherrydev.cherrymarketbe.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionStatus {

    // 400 - Bad Request : 잘못된 요청
    INVALID_INPUT_VALUE(400, "입력 값이 잘못되었습니다."),
    INVALID_TYPE_VALUE(400, "요청 타입이 잘못되었습니다."),
    INVALID_OAUTH_TYPE(400, "소셜 로그인 제공자가 일치하지 않습니다."),
    INVALID_USER_ROLE(400, "존재하지 않는 권한입니다."),
    BLACKLISTED_IP(400, "차단된 IP입니다."),
    NO_FILE_TO_UPLOAD(400, "업로드할 파일이 없습니다."),
    NOT_SOCIAL_ACCOUNT(400, "소셜 가입 계정이 아닙니다."),
    NOT_ALLOWED_CATEGORY_NAME(400, "카테고리 이름은 한글만 가능합니다."),
    NOT_ALLOWED_EMPTY_CONTENT(400, "내용을 입력해주세요."),
    NOT_ALLOWED_EMPTY_SUBJECT(400, "제목을 입력해주세요."),
    NOT_ALLOWED_EMPTY_CATEGORY(400, "카테고리를 입력해주세요."),
    NOT_ALLOWED_EMPTY_DETAIL_CATEGORY(400, "상세카테고리를 입력해주세요."),
    NOT_ALLOWED_EMPTY_DATE(400, "날짜를 입력해주세요."),
    ADDRESS_COUNT_EXCEEDED(400, "주소는 최대 3개까지 등록할 수 있습니다."),
    DEFAULT_ADDRESS_ALREADY_EXISTS(400, "이미 등록된 기본 주소가 있습니다."),


    // 401 - Unauthorized : 비인증(인증 수단이 없음)
    NO_AUTHORIZATION(401, "인증 정보가 없습니다."),
    INVALID_ID_OR_PW(401, "아이디 혹은 비밀번호가 틀렸습니다."),
    INVALID_AUTH_ERROR(401, "지원 되지 않거나 잘못된 인증 수단입니다."),
    INVALID_EMAIL_VERIFICATION_CODE(401, "이메일 인증 코드가 일치하지 않습니다."),

    // 403 - Forbidden : 권한 없음 (서버가 요청을 이해했지만 승인을 거부)
    NOT_COMMENT_OWNER(403, "해당 댓글의 소유자가 아닙니다."),
    NOT_POST_OWNER(403, "해당 게시글의 소유자가 아닙니다."),
    RESTRICTED_ACCOUNT(403, "이용 제한된 계정입니다."),
    PROHIBITED_USERNAME(403, "사용할 수 없는 이름입니다."),
    EMAIL_ALREADY_VERIFIED(403, "이미 인증된 이메일입니다."),
    EMAIL_ALREADY_SENT(403, "이미 이메일이 전송되었습니다. 3분 후에 다시 시도해주세요."),
    BLACKLISTED_TOKEN(403, "무효화된 토큰"),
    EXPIRED_TOKEN(403, "만료된 토큰"),
    EXPIRED_REFRESH_TOKEN(403, "리프레시 토큰 만료"),
    CHANGE_ROLE_FORBIDDEN(403, "소셜 가입 계정은 권한을 변경할 수 없습니다."),
    REVIEW_NOT_ALLOWED_BEFORE_DELIVERY(403, "배송이 완료된 후 리뷰를 작성해주세요."),


    // 404 - Not Found : 잘못된 리소스 접근
    NOT_FOUND_ACCOUNT(404, "존재하지 않는 계정입니다."),
    NOT_FOUND_CREDIT(404, "적립 내역이 존재하지 않습니다."),
    NOT_FOUND_POST(404, "게시글이 존재하지 않습니다."),
    NOT_FOUND_GOODS(404, "상품이 존재하지 않습니다."),
    NOT_FOUND_GRANT_TYPE(404, "존재하지 않는 포인트 지급 타입입니다."),
    NOT_FOUND_ADDRESS(404, "배송지 정보를 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(404, "댓글이 존재하지 않습니다."),
    NOT_FOUND_CATEGORY(404, "카테고리가 존재하지 않습니다."),
    NOT_FOUND_DISCOUNT(404, "등록되지 않은 할인 코드 입니다."),
    NOT_FOUND_MAKER(404, "등록되지 않은 제조사 입니다."),
    NOT_FOUND_COUPON(404, "등록되지 않은 쿠폰 입니다."),
    NOT_FOUND_FILE(404, "파일이 존재하지 않습니다."),
    NOT_FOUND_REDIS_KEY(404, "존재하지 않는 REDIS KEY 입니다."),

    // 405 - Method Not Allowed
    METHOD_NOT_ALLOWED(405, "허용되지 않는 HTTP 메서드입니다."),

    // 409 - Conflict : 중복 데이터
    CONFLICT_ACCOUNT(409, "이미 가입한 계정입니다."),
    DUPLICATED_CATEGORY(409, "이미 등록된 카테고리 입니다."),
    LOCAL_ACCOUNT_ALREADY_EXIST(409, "이미 등록된 계정입니다."),
    DELETED_ACCOUNT(409, "이미 탈퇴한 계정입니다."),
    ALREADY_EXIST_COUPON(409, "이미 등록된 쿠폰입니다."),
    ALREADY_EXIST_REVIEW(409, "이미 등록된 리뷰입니다."),
    ALREADY_EXIST_ANSWER(409, "이미 등록된 답변입니다."),
    ALREADY_EXIST_REPORT(409, "이미 등록된 신고입니다."),

    // 413 - Payload Too Large
    TOO_MANY_FILES(413, "파일은 최대 3개만 업로드 할 수 있습니다."),
    FILE_TOO_LARGE(413, "파일은 최대 3MB까지 업로드 할 수 있습니다."),

    // 415 - Unsupported Media Type
    UNSUPPORTED_FILE_FORMAT(415, "지원하지 않는 파일 형식입니다."),

    // 429 - Too Many Requests
    TOO_MANY_REQUESTS(429, "요청 횟수를 초과하였습니다. 잠시 후 다시 시도해주세요."),

    // 500 - Internal Server Error
    FAIL_TO_SEND_EMAIL(500, "이메일 전송 실패"),
    FAILED_TO_UPLOAD_FILE(500, "파일 업로드 실패"),
    FAILED_HTTP_ACTION(500, "HTTP 요청 실패");

    private final int statusCode;
    private final String message;
}
