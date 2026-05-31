-- 데이터베이스 생성 및 선택 (UTF-8 인코딩 설정 추가)
CREATE DATABASE IF NOT EXISTS sportscenter_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sportscenter_db;

-- 1. 회원 테이블 (members)
CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 식별자',
    name VARCHAR(50) NOT NULL COMMENT '회원 이름 (필수)',
    phone VARCHAR(20) NOT NULL COMMENT '연락처 (필수)',
    gender VARCHAR(10) COMMENT '성별',
    birth_date DATE COMMENT '생년월일',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    notes TEXT COMMENT '기타 특이사항'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='스포츠센터 회원 정보 테이블';

-- 2. 강좌 테이블 (lessons)
CREATE TABLE IF NOT EXISTS lessons (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '강좌 고유 식별자',
    name VARCHAR(100) NOT NULL COMMENT '강좌 이름 (필수)',
    day_of_week VARCHAR(20) COMMENT '강좌 운영 요일',
    time VARCHAR(50) COMMENT '강좌 시간',
    instructor_name VARCHAR(50) COMMENT '담당 강사명',
    capacity INT DEFAULT 0 COMMENT '수강 정원',
    price INT DEFAULT 0 COMMENT '수강료'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='스포츠센터 개설 강좌 테이블';

-- 3. 수강 내역 테이블 (enrollments)
CREATE TABLE IF NOT EXISTS enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '수강 내역 고유 식별자',
    member_id INT NOT NULL COMMENT '회원 식별자 (FK)',
    lesson_id INT NOT NULL COMMENT '강좌 식별자 (FK)',
    start_date DATE COMMENT '수강 시작 일자',
    end_date DATE COMMENT '수강 만료 일자',
    payment_date DATE COMMENT '결제일',
    
    -- 회원 삭제 시 수강 내역도 자동 삭제 (CASCADE)
    CONSTRAINT fk_enrollments_member FOREIGN KEY (member_id) 
        REFERENCES members(id) ON DELETE CASCADE,
        
    -- 수강 회원이 있는 강좌는 삭제 불가 (RESTRICT)
    CONSTRAINT fk_enrollments_lesson FOREIGN KEY (lesson_id) 
        REFERENCES lessons(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원별 수강 등록 및 이력 테이블';
