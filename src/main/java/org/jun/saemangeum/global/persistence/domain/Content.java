package org.jun.saemangeum.global.persistence.domain;

// H2의 엔티티로 쓰이게 될 클래스
public class Content {
    private Long id;
    private String title;
    private Category category;
    private String image;
    private String introduction;
}
