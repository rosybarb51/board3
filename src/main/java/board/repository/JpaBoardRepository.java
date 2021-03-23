package board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import board.entity.BoardEntity;
import board.entity.BoardFileEntity;

// mybatis의 mapper 파일 및 xml 파일이 jpa에서는 Repository 파일이라고 생각하면 됨
// 스프링데이터의 JPA 레파지토리 인터페이스

// Repository 인터페이스를 상속 받아서 사용하는 것이 CrudRepository 이다. 
// 그리고 다시 CrudRepository 를 상속받아서 사용하는 것이 PagingAndSortingRepository 이다.
// 그리고 다시 PagingAndSortingRepository 을 상속 받아 사용하는 것이 JpaRepository 이다. 
// 즉, Repository -> CrudRepository -> PagingAndSortingRepository -> JpaRepository 의 순서대로 상속받아 사용한다.

// Repository 인터페이스는 아무런 기능이 없음(잘 사용하지 않음)
// CrudRepository 인터페이스는 기본적인 CRUD(create, read, update, delete) 기능을 제공함
// PagingAndSortingRepository 인터페이스는 CrudRepository 인터페이스의 기능에 페이징 및 정렬 기능이 추가된 인터페이스임
// JpaRepository 인터페이스는 PagingAndSortingRepository 의 기능에 JPA에 특화된 기능까지 추가된 인터페이스

// 밑으로 내려올 수록 기능이 많다고 생각하면 됨, 우리가 별로 사용할게 없다고 하면 그냥 Repository를 사용하면 되고, 보통은 CrudRepository를 많이 사용한다. 그래서 우리도 밑에서처럼 CrudRepository를 사용했다. 


// CrudRepository 에서 지원하는 메서드

// <S extends T> save(s entity) : 주어진 엔터티를 저장
// <S extends T> Iterable<S> saveAll(Iterable<S> entities) : 주어진 엔터티 목록을 저장
// Option<T> findById(Id id) : 주어진 아이디로 식별된 엔터티를 반환
// boolean existsById(Id id) : 주어진 아이디로 식별된 엔터티가 존재하는지를 반환
// Iterable<T> findAll() : 모든 엔터티를 반환
// Iterable<T> findAllById(Iterable<ID> ids) : 주어진 아이디 목록에 맞는 모든 엔터티 목록을 반환
// long count() : 사용 가능한 엔터티의 개수를 반환
// void deleteById(Id id) : 주어진 아이디로 식별된 엔터티를 삭제함
// void delete(T entity) : 주어진 엔터티를 삭제함
// void deleteAll(Iterable<? extends T> entities) : 주어진 엔터티 목록으로 식별된 엔터티를 모두 삭제
// void deleteAll : 모든 엔터티를 삭제

// 스프링 데이터 JPA 는 쿼리 메서드를 지원하여 원하는 형태의 쿼리를 생성하는 기능 존재함
// 그러니까, 위의 메서드만 가지고서는 우리가 원하는 쿼리문을 다 작성할 수 없다, 그래서 아래 형태처럼 JPA가 지원하는 쿼리 메서드도 사용한다.
// find...By, read...By, query...By, count...By 와 같은 형태로 사용
// 첫 번째 By의 뒷쪽은 컬럼명, 앞쪽은 crud 중 실행할 형태를 선택
// JPA의 쿼리 메서드를 통해서 제목을 검색 시 findByTitle(String title) 형태로 메서드를 작성
// 2개 이상의 속성을 조합하여 검색 시 AND 키워드를 사용
// findByTitleAndContent(String title, String contents) 이런 식으로 사용..

// JPA에서 사용하는 연산자 목록
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
// =>공식 문서 참고하기. 공식문서를 직접 읽어보는 것이 제일 좋음!
// AND 사용 시 실제 쿼리로 변환 (우리가 쓴 메서드 : findByLastnameAndFirstname -> 출력 : where x.lastname = ?1 and x.firstname = ?2 ) 이런식으로 변환됨.. (예시의 x는 프로그래밍 형태로 변하는 것이라서, 객체명 대신 x를 넣어줬음)

// 제목(안녕하세요), 글쓴이(test)를 가지고 검색할 메서드를 만들려면, -> findByTitleAndCreatedId(String title, String userId) 이렇게 작성해준다. 그러면 => select * from t_jpa_board where title = '안녕하세요' and created_id = 'test' 자동으로 이런 형태로 변환이 된다.

// Query 어노테이션 사용
// 메서드의 이름이 복잡하거나 쿼리 메서드로 표현하기 힘들 경우 @Query 어노테이션을 사용하여 쿼리를 직접 작성할 수 있음, 아래의 두 가지 방법이 존재하는데 결과는 같음.

// 첫 번째 방식============
// @Query("SELECT file FROM BoardFileEntity file WHERE board_idx = ?1 AND idx = ?2")
// BoardFileEntity findBoardFile(int boardIdx, int idx); 이런 식으로 만들어주면 된다.
// [?숫자] 형식으로 파라미터를 지정, 메서드의 매개변수 순서대로 ?1, ?2에 할당됨
// boardIdx는 ?1에 할당, idx는 ?2에 할당

// 두 번째 방식============
// @Query("SELECT file FROM BoardFileEntity file WHERE board_idx = :boardIdx AND idx = :idx")
// BoardFileEntity findBoardFile(@Param("boardIdx") fint boardIdx, @Param("idx") int idx);
// :[변수명] 형식으로 파라미터를 지정, 변수명은 @Param 어노테이션에 대응함
// :boardIdx 는 @Param("boardIdx")에 할당, :idx는 @Param("idx")에 할당


public interface JpaBoardRepository extends CrudRepository<BoardEntity, Integer> {
	List<BoardEntity> findAllByOrderByBoardIdxDesc();
	
//	파일 업로드를 위한 추가 작성---------------
//	쿼리문 사용
	@Query("SELECT file FROM BoardFileEntity file WHERE board_idx = :boardIdx AND idx = :idx")
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);
}



















