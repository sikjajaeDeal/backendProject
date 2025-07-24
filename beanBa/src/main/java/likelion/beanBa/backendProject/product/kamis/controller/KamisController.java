package likelion.beanBa.backendProject.product.kamis.controller;

import java.util.List;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisGetAllResponseDTO;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisSearchResponseDTO;
import likelion.beanBa.backendProject.product.kamis.service.KamisService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kamis")
public class KamisController {

  private final KamisService kamisService;

  @GetMapping("/all")
  public ResponseEntity<List<KamisGetAllResponseDTO>> getAllKamisData() {
    return ResponseEntity.ok().body(kamisService.getAllKamisData());
  }


}
