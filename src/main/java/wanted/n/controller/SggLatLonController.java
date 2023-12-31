package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wanted.n.dto.SggLatLonDTO;
import wanted.n.dto.SggLatLonResponseDTO;
import wanted.n.service.SggLatLonService;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static wanted.n.enums.FileValues.HEADER_VALUES_FILE;
import static wanted.n.enums.FileValues.TEXT_CSV;

@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Api(tags = "시군구 File API", description = "시군구 파일 관련 API")
@RestController
public class SggLatLonController {

    private final SggLatLonService sggLatLonService;

    @PostMapping
    @ApiOperation(value = "csv 업데이트", notes = "csv 파일을 읽어서 데이터베이스에 저장")
    public ResponseEntity<Void> readCsv(MultipartFile csvFile) {

        sggLatLonService.updateCsv(csvFile);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/template")
    @ApiOperation(value = "csv 양식 다운로드", notes = "csv 양식을 다운로드")
    public ResponseEntity<InputStreamResource> downloadTemplate() {

        InputStreamResource sggCsvFile = sggLatLonService.getTemplate();

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, HEADER_VALUES_FILE)
                .contentType(MediaType.parseMediaType(TEXT_CSV))
                .body(sggCsvFile);
    }

    @GetMapping
    @ApiOperation(value = "시군구 목록조회", notes = "시군구 목록을 조회")
    public ResponseEntity<SggLatLonResponseDTO> getSggLatLons(){

        List<SggLatLonDTO> sggLatLons = sggLatLonService.getSggLatLons();

        return ResponseEntity.status(OK).body(SggLatLonResponseDTO.from(sggLatLons));
    }
}

