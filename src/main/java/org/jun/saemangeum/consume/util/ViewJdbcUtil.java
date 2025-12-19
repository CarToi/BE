package org.jun.saemangeum.consume.util;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewJdbcUtil {

    private static final String CREATE_CONTENTS_VIEW = """
            CREATE VIEW IF NOT EXISTS contents_view AS\s
            SELECT * FROM contents;\s
            """;
    private static final String CREATE_VECTORS_VIEW = """
            CREATE VIEW IF NOT EXISTS vectors_view AS\s
            SELECT * FROM vectors;\s
            """;
    private static final String DROP_CONTENTS_VIEW = """
            DROP VIEW IF EXISTS contents_view;\s
            """;
    private static final String DROP_VECTORS_VIEW = """
            DROP VIEW IF EXISTS vectors_view;\s
            """;

    private final JdbcTemplate jdbcTemplate;

    // MySQL은 DDL에서는 자동 커밋이 되므로 트랜잭션 할당 비권장
    // 멱등성 리팩토링
    public void createViews() {
        jdbcTemplate.execute(CREATE_CONTENTS_VIEW);
        jdbcTemplate.execute(CREATE_VECTORS_VIEW);
    }

    public void dropViews() {
        jdbcTemplate.execute(DROP_CONTENTS_VIEW);
        jdbcTemplate.execute(DROP_VECTORS_VIEW);
    }
}
