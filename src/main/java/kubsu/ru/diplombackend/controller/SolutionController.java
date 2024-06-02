package kubsu.ru.diplombackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import kubsu.ru.diplombackend.controller.dto.request.ExportRequest;
import kubsu.ru.diplombackend.controller.dto.request.SolutionTaskRequest;
import kubsu.ru.diplombackend.services.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Контроллер для функциональной составляющей Студентов
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solution")
public class SolutionController {
    private final SolutionService solutionService;

    @PostMapping("/task/send")
    public ResponseEntity<?> sendSolution(@RequestBody SolutionTaskRequest request) throws JsonProcessingException {
        return ResponseEntity.ofNullable(solutionService.sendStudentTask(request));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getSolutions(@PathVariable Long taskId) {
        return ResponseEntity.ofNullable(solutionService.getSolutionsByTaskId(taskId));
    }

    @PostMapping("/export/{solutionId}")
    public void exportSolution(HttpServletResponse response, @PathVariable Long solutionId, @RequestBody ExportRequest request) throws IOException {
        File file = solutionService.exportSolution(solutionId, request.getFio());
        ServletOutputStream outputStream = response.getOutputStream();
        new FileInputStream(file).transferTo(outputStream);
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(file.getName())
                .build();
        response.setHeader("Content-Disposition", contentDisposition.toString());
        response.flushBuffer();
    }
}
