package kubsu.ru.diplombackend.controller;

import kubsu.ru.diplombackend.entity.TestCase;
import kubsu.ru.diplombackend.services.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-case")
public class TestCaseController {
    private final TestCaseService testCaseService;

    @PostMapping("/add/{taskId}")
    public ResponseEntity<?> addTestCase(@PathVariable Long taskId, @RequestBody TestCase testCase) {
        return ResponseEntity.ok(testCaseService.addTestCase(taskId, testCase));
    }

    @PostMapping("/saveAll")
    public ResponseEntity<?> saveTestCase(@RequestBody List<TestCase> body) {
        return ResponseEntity.ok(testCaseService.saveAll(body));
    }

    @GetMapping("/byTaskId/{taskId}")
    public ResponseEntity<?> getTestCasesByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(testCaseService.getAllTestsByTaskId(taskId));
    }

}
