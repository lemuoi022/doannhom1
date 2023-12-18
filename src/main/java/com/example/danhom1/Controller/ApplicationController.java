package com.example.danhom1.Controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.danhom1.Exception.StorageFileNotFoundException;
import com.example.danhom1.Model.ResponseMessage;
import com.example.danhom1.Model.FileInfo;

import com.example.danhom1.Model.Role.RoleName;
import com.example.danhom1.Model.Role.RoleRepo;
import com.example.danhom1.Model.Storage.Storage;
import com.example.danhom1.Model.User.User;
import com.example.danhom1.Model.User.UserRepo;
import com.example.danhom1.Model.User.UserService;
import com.example.danhom1.Model.UserDTO.UserLoginDto;
import com.example.danhom1.Model.UserDTO.UserRegisterDto;
import com.example.danhom1.Model.UserFile.UserFileService;
import com.example.danhom1.Model.UserStorage.UserStorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.example.danhom1.Model.Storage.StorageService;

import javax.naming.SizeLimitExceededException;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class ApplicationController {
	private final StorageService storageService;

//    @GetMapping("/files")
//	@ResponseBody
//	public List<String> listUploadedFiles() {
//		return storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(ApplicationController.class,
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

	private final UserService userService;
	private final UserRepo userRepo;
	private final UserStorageService userStorageService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final Storage storage;
	private final RoleRepo roleRepo;
	private final UserFileService userFileService;

	@PostMapping("/login")
	public ResponseEntity<ResponseMessage> login (@RequestBody @NotNull UserLoginDto userLoginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
		SecurityContextHolder.getContext().setAuthentication(authentication);
//		System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]);
		storageService.setRootPath(Path.of(storage.getPPath() + File.separator + userRepo.findUserByEmail(userLoginDto.getEmail()).getName()));
		return ResponseEntity.ok(new ResponseMessage("User logged in successfully."));
	}

	@PostMapping("/register")
	public ResponseEntity<ResponseMessage> registerUser(@RequestBody @Valid @NotNull UserRegisterDto userRegisterDto) {
		User user = new User();
		user.setName(userRegisterDto.getUsername());
		user.setPass(passwordEncoder.encode(userRegisterDto.getPassword()));
		user.setEmail(userRegisterDto.getEmail());
		user.setRole(Collections.singleton(roleRepo.findByName(RoleName.ROLE_ADMIN)));
		try {
			userService.RegisterNewUser(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
		}
		userStorageService.RegisterNewUser(user);
		storageService.init(Path.of(storage.getPPath() + File.separator + user.getName()));
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("User registered successfully."));
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseMessage> logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Logout successful."));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
			userFileService.store(storageService.load((storage.getPPath() + File.separator + file.getOriginalFilename())),
					userRepo.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getUserStorage()
			);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (SizeLimitExceededException e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage() + "Size exceeded!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		} catch (NullPointerException e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: Unauthorized Access.";
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(message));
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
					.fromMethodName(ApplicationController.class, "getFile", path.getFileName().toString()).build().toString();
			return new FileInfo(filename, url);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filename) {
		try {
			Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
		} catch (StorageFileNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
		}
	}
}

//	@ExceptionHandler(StorageFileNotFoundException.class)
//	public String handleStorageFileNotFound(@NonNull RedirectAttributes redirectAttributes) {
//		redirectAttributes.addFlashAttribute("message", "There was no file!");
//		return "redirect:/upload";
//	}
