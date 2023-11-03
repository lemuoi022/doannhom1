package com.example.danhom1.Controller;

//import java.io.IOException;
import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.danhom1.Exception.StorageFileNotFoundException;
import com.example.danhom1.Storage.StorageService;

@Controller
@AllArgsConstructor
public class UploadController {
    private final StorageService storageService;

    @GetMapping("/upload")
	@ModelAttribute
	public String listUploadedFiles(@NonNull Model model) {
		model.addAttribute("files", storageService.loadAll()
        .map(path -> MvcUriComponentsBuilder.fromMethodName(UploadController.class, 
        "serveFile", path.getFileName().toString()).build().toUri().toString()).
        collect(Collectors.toList()));
		return "upload";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		if (file == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        storageService.store(file);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/upload";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound() {
		return ResponseEntity.notFound().build();
	}
}

