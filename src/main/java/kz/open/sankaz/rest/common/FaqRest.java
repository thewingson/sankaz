package kz.open.sankaz.rest.common;

import kz.open.sankaz.repo.FaqRepo;
import kz.open.sankaz.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/faqs")
public class FaqRest {

    private final FaqRepo faqRepo;

    @Autowired
    public FaqRest(FaqRepo faqRepo) {
        this.faqRepo = faqRepo;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            return ResponseModel.success(faqRepo.findAll(PageRequest.of(page, size)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<?> getOne(@PathVariable("faqId") Long faqId) {
        try {
            return ResponseModel.success(faqRepo.findById(faqId));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }
}
