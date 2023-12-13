package com.cherrydev.cherrymarketbe.goods;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.factory.GoodsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;

@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
public class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                          .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                          .build();
    }

    // @Test
    // @Transactional
    // public void 상품_저장_테스트() throws Exception {
    //     GoodsDto goodsDto = GoodsFactory.createGoodsDto();
    //     String goodsDtoJson = objectMapper.writeValueAsString(goodsDto);
    //
    //     // GoodsDto를 MultipartFile 형식으로 변환
    //     MockMultipartFile goodsDtoFile = new MockMultipartFile(
    //             "goodsDto",
    //             "",
    //             MediaType.APPLICATION_JSON_VALUE,
    //             goodsDtoJson.getBytes()
    //     );
    //
    //     // 임의의 바이트 배열로 이미지 데이터를 생성
    //     byte[] imageData = "임의의 이미지 데이터".getBytes();
    //     MockMultipartFile imageFile = new MockMultipartFile(
    //             "images",
    //             "image.jpg",
    //             MediaType.IMAGE_JPEG_VALUE,
    //             imageData
    //     );
    //
    //     mockMvc.perform(MockMvcRequestBuilders.multipart("/api/goods/save")
    //                             .file(goodsDtoFile)
    //                             .file(imageFile))
    //             .andExpect(status().isOk())
    //             .andDo(document("goods-save",
    //                     resourceDetails().description("상품 저장 테스트")
    //             ));
    // }


    @Test
    @Transactional
    public void 모든_상품_조회_테스트() throws Exception {

        String requestBody = Jackson.toJsonString(GoodsFactory.createGoodsDto());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods/listAll")
                                .param("sortBy", "priceAsc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))

                .andExpect(status().isOk())
                .andDo(document("goods-list-all",
                        resourceDetails().description("모든 상품 조회 테스트")
                ));
    }
}

