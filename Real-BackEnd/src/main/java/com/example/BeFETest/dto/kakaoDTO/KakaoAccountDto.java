package com.example.BeFETest.dto.kakaoDTO;

import lombok.Data;

@Data
public class KakaoAccountDto{

    private Long id;
    private Account kakao_account;
    private Properties properties;

    @Data
    public static class Properties{
        private String nickname;
        private String profile_image;
    }
    @Data
    public static class KakaoAccount{
        private Profile profile;
        private String email;

        @Data
        public static class Profile{
            private String nickname;
            private String thumbnail_image_url;
            private String profile_image_url;
        }
    }
}