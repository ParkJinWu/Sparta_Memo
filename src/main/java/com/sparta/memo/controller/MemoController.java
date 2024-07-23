package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/*
      @RestController  ➡️ @Controller와 @ResponseBody를 결합한 역할

      RequestDto를 통해 클라이언트로부터 데이터를 받고, 이를 엔티티로 변환한 후 데이터베이스에 저장
* */


@RestController
@RequestMapping("/api")
public class MemoController {

    //DB대체의 구현체
    private final Map<Long,Memo> memoList = new HashMap<>();

    // 메모장 생성 api
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto){

        // RequestDto ➡️ Entity로 변환 (DB와 소통해야함)
        Memo memo = new Memo(requestDto);

        // Memo Max ID Check
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(memo.getId(),memo);

        // Entity ➡️ RequestDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    // 메모장 조회 api
    @GetMapping("/memos")
    // Memo는 당연히 여러개일 수 가 있기 때문에 List로 반환.
    public List<MemoResponseDto> getMemos(){
        // Map to List
        // 메모들을 vlause()메서드로 값만 가져오고 stream API를 사용하여 하나 씩
        // MemoResponseDto로 변환 후 List로 변환
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new)
                .toList();
        return responseList;
    }

}