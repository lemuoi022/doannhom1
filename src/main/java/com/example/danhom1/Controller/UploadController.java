package com.example.danhom1.Controller;

//import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
import com.example.danhom1.Model.ResponseMessage;
import com.example.danhom1.Model.FileInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.example.danhom1.Model.Storage.StorageService;

import javax.naming.SizeLimitExceededException;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class UploadController {
	private final StorageService storageService;

//    @GetMapping("/files")
//	@ResponseBody
//	public List<String> listUploadedFiles() {
//		return storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(UploadController.class,
//        "serveFile", path.getFileName().toString()).build().toUri().toString()).
//        collect(Collectors.toList());
//	}

//	@GetMapping("/files/{filename:.+}")
//	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//		try {
//			Resource file = storageService.loadAsResource(filename);
//			if (file == null){
//				throw new StorageFileNotFoundException("File doesn't exist.");
//			}
//			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//		} catch (StorageFileNotFoundException e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}

//	@GetMapping("/limit")
//	@ResponseBody
//	public String viewLimit(){
//		return "LIMIT EXCEEDED BUY MORE NOW";
//	}
//
//	@PostMapping("/limit")
//	public ModelAndView returnToUpload(@NonNull Model Model){
//		Model.addAttribute("");
//		return new ModelAndView("redirect:/upload");
//	}

//	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        try {
//			log.info("Handling {}...", file);
//			storageService.store(file);
//			log.info("Done");
//			return ResponseEntity.ok().body(file.getOriginalFilename());
//		} catch (SizeLimitExceededException e) {
//			log.warn(e.getMessage());
//			return ResponseEntity.badRequest().body("Limit exceeded");
//        }
//		catch (StorageException e) {
//			log.error(e.getMessage());
//			return ResponseEntity.internalServerError().body("Failed to store file");
//		}
//	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (SizeLimitExceededException e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage() + "Size exceeded!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(UploadController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = (Resource) storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
}

//	@ExceptionHandler(StorageFileNotFoundException.class)
//	public String handleStorageFileNotFound(@NonNull RedirectAttributes redirectAttributes) {
//		redirectAttributes.addFlashAttribute("message", "There was no file!");
//		return "redirect:/upload";
//	}
