create table bc_member(
member_idx int unsigned auto_increment, 
member_id varchar(30) not null unique,
member_pw varchar(100) not null,
member_name varchar(30) not null,
member_phone varchar(20) not null unique,
member_nickname varchar(30) not null unique,
member_img varchar(50),
member_img_save varchar(50),
option_agree decimal(1) default 0,
reg_date datetime default now(),
last_mod_date datetime default now(),
member_del decimal(1) default 0,
grade decimal(1) default 0,
constraint bc_member_pk primary key (member_idx)
);

create table bc_notice(
notice_idx int unsigned auto_increment, -- 게시글 번호
title varchar(50) not null, -- 제목
content varchar(1000) not null, -- 내용
originfile_name varchar(50), -- 원본 파일명
savefile_name varchar(50), -- 저장 파일명
post_date datetime default now(), -- 등록일
update_date datetime default now(), -- 수정일
del_or_not decimal(1) default 1, -- 게시글 유지 여부
constraint bc_notice_pk primary key (notice_idx) -- 기본키
);

create table bc_bakery(
bakery_idx int unsigned auto_increment, 
bakery_name varchar(100) not null,
bakery_phone varchar(20) not null unique,
bakery_postcode varchar(30) not null,  -- 우편번호
bakery_address varchar(100) not null,
bakery_detail_address varchar(100) not null,
bakery_homepage varchar(100),
bakery_img varchar(50),
bakery_img_save  varchar(50),
bakery_del decimal(1) default 0,
member_idx int unsigned,
constraint bc_bakery_pk primary key (bakery_idx),
constraint bc_bakery_fk foreign key (member_idx) references bc_member (member_idx) 
);

create table bc_bread(
bread_idx int unsigned auto_increment, 
bread_name varchar(100) not null,
bread_price int unsigned not null,
bread_content varchar(100) not null,
bread_time1 varchar(30),
bread_time2 varchar(30),
bread_time3 varchar(30),
bread_img varchar(50),
bread_img_save varchar(50),
bread_del decimal(1) default 0,
bread_status decimal(1) default 0,
bakery_idx int unsigned,
constraint bc_bread_pk primary key (bread_idx),
constraint bc_bread_fk foreign key (bakery_idx) references bc_bakery(bakery_idx) 
);

CREATE TABLE bc_review (
    review_idx INT UNSIGNED AUTO_INCREMENT, -- 기본키. 리뷰번호
    order_idx VARCHAR(50), -- 주문번호
    member_idx INT UNSIGNED,-- 외래 : 닉네임
    bread_idx INT UNSIGNED, -- 외래 : 상품이미지
    review_content VARCHAR(400) NOT NULL, -- 리뷰내용
    review_post_date DATETIME DEFAULT NOW(), -- 리뷰작성일
    review_update_date DATETIME DEFAULT NOW(), -- 수정일
    review_del_or_not DECIMAL(1) DEFAULT 1, -- 게시글 유지 여부
    view_count int default 0, -- 조회수 카운트 추가
    savefile_name varchar(50) default null, -- 파일추가
    originfile_name varchar(50) default null, -- 파일추가
    CONSTRAINT bc_review_pk PRIMARY KEY (review_idx),
    CONSTRAINT bc_review_fk FOREIGN KEY (member_idx) REFERENCES bc_member(member_idx),
    CONSTRAINT bc_review_fk2 FOREIGN KEY (bread_idx) REFERENCES bc_bread(bread_idx)
); 

CREATE TABLE bc_order (
  order_idx varchar(50), -- 주문번호
  payment_status INT DEFAULT 0, -- 결제상태 ->order_status에서 변경
  amount int, -- 총 결제액
  payment_date DATETIME DEFAULT NOW(),-- 결제일 order_date에서 변경
  member_idx INT UNSIGNED,
  CONSTRAINT bc_order_pk PRIMARY KEY (order_idx),
  CONSTRAINT bc_order_fk FOREIGN KEY (member_idx) REFERENCES bc_member(member_idx));
  
CREATE TABLE bc_item (
  item_idx int unsigned auto_increment, -- 주문 제품 번호
  bread_idx INT UNSIGNED,-- 상품 외래키
  bakery_idx INT UNSIGNED,-- 가게 외래키
  review_status decimal(1) default 0,
  bread_count int,
  order_idx varchar(50), -- 주문내역 외래키
  CONSTRAINT bc_order_pk PRIMARY KEY (item_idx),
  CONSTRAINT bc_order_fk2 FOREIGN KEY (bread_idx) REFERENCES bc_bread(bread_idx),
  CONSTRAINT bc_order_fk3 FOREIGN KEY (order_idx) REFERENCES bc_order(order_idx),
  CONSTRAINT bc_order_fk4 FOREIGN KEY (bakery_idx) REFERENCES bc_bakery(bakery_idx));

create table bc_likes(
    likes_idx int unsigned auto_increment primary key,
    likes_check decimal(1) default 1, -- 좋아요 중복방지
    bakery_idx int unsigned,  -- 좋아요된 매장
    member_idx int unsigned, -- 좋아요한 사용자

    CONSTRAINT bc_bakeryLikes_fk FOREIGN KEY (bakery_idx) REFERENCES bc_bakery(bakery_idx) on delete cascade, -- 매장 삭제되면 하트도 사라짐
    CONSTRAINT bc_bakeryLikes_fk2 FOREIGN KEY (member_idx) REFERENCES bc_member(member_idx) on delete cascade -- 사용자 탈퇴시 하트 사라짐
    );

create table bc_cart(
cart_idx int unsigned auto_increment, 
member_idx int unsigned,
bread_idx int unsigned,
bread_count int,
constraint bc_cart_pk primary key (cart_idx),
constraint bc_cart_fk foreign key (member_idx) references bc_member(member_idx),
constraint bc_cart_fk2 foreign key (bread_idx) references bc_bread(bread_idx)
);
alter table bc_bread add bread_stock int;