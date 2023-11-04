package com.example.danhom1.Controller;

//import java.io.IOException;
import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
import com.example.danhom1.Exception.StorageException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.danhom1.Exception.StorageFileNotFoundException;
import com.example.danhom1.Storage.StorageService;

import javax.naming.SizeLimitExceededException;

@RestController
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
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		try {
			Resource file = storageService.loadAsResource(filename);
			if (file == null){
				throw new StorageFileNotFoundException("File doesn't exist.");
			}
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
		} catch (StorageFileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File doesn't exist.",e);
		}
	}

	@GetMapping("/limit")
	@ModelAttribute
	public String viewLimit(@NonNull Model model){
		model.addAttribute("message","LIMIT EXCEEDED BUY MORE NOW");
		return "limit";
	}

	@PostMapping("/limit")
	public ModelAndView returnToUpload(@NonNull Model model){
		model.addAttribute("");
		return new ModelAndView("redirect:/upload");
	}

	@PostMapping("/upload")
	public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file, @NonNull RedirectAttributes redirectAttributes) {
        try {
            storageService.store(file);
			redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
			//this part is temporary
			//will return response code in the future
			return new ModelAndView("redirect:/upload");
		} catch (SizeLimitExceededException e) {
			return new ModelAndView("redirect:/limit");
        }
//		catch (StorageException e) {
//			redirectAttributes.addFlashAttribute("message", "Empty or bad file.");
//			return new ModelAndView("redirect:/upload",HttpStatus.INTERNAL_SERVER_ERROR);
//		}
	}


//	@ExceptionHandler(StorageFileNotFoundException.class)
//	public String handleStorageFileNotFound(@NonNull RedirectAttributes redirectAttributes) {
//		redirectAttributes.addFlashAttribute("message", "There was no file!");
//		return "redirect:/upload";
//	}
}
