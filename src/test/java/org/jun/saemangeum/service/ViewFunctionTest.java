package org.jun.saemangeum.service;

import org.jun.saemangeum.consume.domain.swap.VectorView;
import org.jun.saemangeum.consume.service.swap.SwapViewService;
import org.jun.saemangeum.consume.util.ViewJdbcUtil;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.global.repository.VectorRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ViewFunctionTest {

    @Autowired
    private ViewJdbcUtil viewJdbcUtil;

    @Autowired
    private SwapViewService swapViewService;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private VectorRepository vectorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // test ddl : update
    private static final String SQL_1 = """
            DROP TABLE IF EXISTS contents_view;
            """;
    private static final String SQL_2 = """
            DROP TABLE IF EXISTS vectors_view;
            """;

    @BeforeEach
    void setUp() {
        Content content = Content.builder()
                .title("title")
                .position("where")
                .category(Category.CULTURE)
                .build();
        Content savedContent = contentRepository.save(content);

        Vector vector = Vector.builder()
                .content(content)
                .build();
        Vector savedVector = vectorRepository.save(vector);
        savedContent.setVector(savedVector);

        // JPA DDL 정책 update로 인한 테이블 생성 오류 제거
        jdbcTemplate.execute(SQL_1);
        jdbcTemplate.execute(SQL_2);

        viewJdbcUtil.createViews();
    }

    @AfterEach
    void tearDown() {
        viewJdbcUtil.dropViews();
        contentRepository.deleteAll();
    }

    @Test
    @DisplayName("뷰 기반 생성 및 객체 연관관계 설정 테스트")
    void test() {
        List<VectorView> vectorViews = swapViewService.getVectorViews();
        Optional<VectorView> vectorViewOptional = vectorViews.stream().findFirst();
        Assertions.assertTrue(vectorViewOptional.isPresent());

        VectorView vectorView = vectorViewOptional.get();
        Assertions.assertEquals(vectorView.getContentView().getTitle(), "title");
    }
}
