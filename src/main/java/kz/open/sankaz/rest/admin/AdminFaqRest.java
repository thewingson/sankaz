package kz.open.sankaz.rest.admin;

import kz.open.sankaz.model.Faq;
import kz.open.sankaz.pojo.filter.FaqCreateFilter;
import kz.open.sankaz.repo.FaqRepo;
import kz.open.sankaz.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/faqs")
public class AdminFaqRest {

    private final FaqRepo faqRepo;

    @Autowired
    public AdminFaqRest(FaqRepo faqRepo) {
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

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody FaqCreateFilter filter) {
        try {
            Faq faq = new Faq();
            faq.setQuestion(filter.getQuestion());
            faq.setAnswer(filter.getAnswer());
            faqRepo.save(faq);
            return ResponseModel.success(faq);
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<?> editOneById(@PathVariable("faqId") Long faqId,
                                         @Valid @RequestBody FaqCreateFilter filter) {
        try {
            Optional<Faq> byId = faqRepo.findById(faqId);
            byId.ifPresent(faq -> {
                faq.setQuestion(filter.getQuestion());
                faq.setAnswer(filter.getAnswer());
                faqRepo.save(faq);
            });
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "faqId") Long faqId) {
        try{
            faqRepo.deleteById(faqId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
