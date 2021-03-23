package board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.common.FileUtil;
import board.entity.BoardEntity;
import board.entity.BoardFileEntity;
import board.repository.JpaBoardRepository;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

	@Autowired
	private JpaBoardRepository jpaRepo;
	
//	파일 업로드를 위한 추가 작성 ------
	@Autowired
//	아래의 FileUtil은 우리가 만든 파일이름임. 
	private FileUtil fileUtil;
	
	@Override
	public List<BoardEntity> jpaSelectListBoard() throws Exception {
		return jpaRepo.findAllByOrderByBoardIdxDesc();
	}
	
//	추가 작성-------
	@Override
	public BoardEntity jpaSelectDetailBoard(int boardIdx) throws Exception {
//		Optional 클래스는 자바 1.8버전에 추가 됨
//		Optional 클래스는 어떤 객체의 값으로 Null이 아닌 어떤 값을 가지고 있음, Optional 클래스는 절대 null 이 아님!
		Optional<BoardEntity> optional = jpaRepo.findById(boardIdx);
		
//		isPresent() 메서드를 사용하여 객체의 값이 존재하는지 여부 확인
		if (optional.isPresent()) {
//			optional.get() 메서드를 사용하여 데이터를 불러옴
			BoardEntity jpaBoard = optional.get();
//			값을 따로 올릴 수 없어서 이렇게 썼다. 카운트 1 올려주는 것.. 데이터베이스에 직접 올려서 다시로딩해오는 것이 아니라, 어차피 연동돼있으니까 아래와 같이 여기서 해줬다. 
			jpaBoard.setHitCnt(jpaBoard.getHitCnt() + 1);
			jpaRepo.save(jpaBoard);
			
			return jpaBoard;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	@Override
//	파일 업로드를 위한 추가 작성 ------
	public void saveBoard(BoardEntity jpaBoard, MultipartHttpServletRequest multiFile) throws Exception {
		jpaBoard.setCreatedId("admin");
		List<BoardFileEntity> list = fileUtil.parseFileInfo(jpaBoard.getBoardIdx(), multiFile);
		
		if (CollectionUtils.isEmpty(list) == false) {
			jpaBoard.setFileList(list);
		}
		
//		CrudRepository 인터페이스의 save() 메서드는 insert와 update 기능을 동시에 수행함
		jpaRepo.save(jpaBoard);
	}
	
	@Override
	public void deleteBoard(int boardIdx) {
		jpaRepo.deleteById(boardIdx);
	}
	
//	파일 업로드를 위한 추가 작성 -----------
	@Override
	public BoardFileEntity jpaSelectBoardFileInfo(int boardIdx, int idx) throws Exception {
		BoardFileEntity boardFile = jpaRepo.findBoardFile(boardIdx, idx);
		
		return boardFile;
	}
	
}






























