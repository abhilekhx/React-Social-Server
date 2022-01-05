package com.revature.users.profiles;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.ProfileNotFoundException;
import com.revature.exceptions.WrongUserException;
import com.revature.users.User;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	/*
	 * Get Profile from the database by ID. Requires the integer id in the URL call
	 * returns Optional<Profile>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Profile> findProfileById(@PathVariable String id) {
		try {
			return ResponseEntity.ok(profileService.findProfileById(id));
		} catch (ProfileNotFoundException e) {
//			e.printStackTrace();
			return ResponseEntity.status(404).build();
		}
	}

	/*
	 * Get the profile by the author's ID.
	 * returns the profile
	 */
	@GetMapping("/getByAuthor/{id}")
	public ResponseEntity<Profile> findProfileByAuthor(@PathVariable String id) {
		try {
			User query = new User();
			query.setId(id);
			return ResponseEntity.ok(profileService.findUsersProfile(query));
		} catch (ProfileNotFoundException e) {
			//e.printStackTrace();
			return ResponseEntity.status(404).build();
		}
	}

	/*  Must be give a Profile object in the Request body.
		Updates the Profile in the database.
		Returns the updated Profile.
	 */
	@PutMapping("/update")
	public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile, @AuthenticationPrincipal User user) {
		try {
			return ResponseEntity.ok(profileService.updateProfile(profile, user));
		} catch (WrongUserException e) {
//			e.printStackTrace();
			return ResponseEntity.status(403).build();
		}
	}

	/*
	 * Get Profile of one specific user. requires a User object returns
	 * Optional<Profile>
	 */
	@GetMapping("/getUsersProfile")
	public ResponseEntity<Profile> findThisUsersProfile(@AuthenticationPrincipal User user) {
		try {
			return ResponseEntity.ok(profileService.findUsersProfile(user));
		} catch (ProfileNotFoundException e) {
//			e.printStackTrace();
			return ResponseEntity.status(404).build();
		}
	}

	/*  Must be provided an id in the URL
		Verifies whether or not the logged in user is the owner of that Profile
		Returns boolean value
	 */
	@GetMapping("/checkProfileOwnership/{id}")
	public ResponseEntity<Boolean> checkProfileOwnership(@PathVariable String id, @AuthenticationPrincipal User user) {
		try {
			return ResponseEntity.ok(profileService.checkProfileOwnership(id, user));
		} catch (ProfileNotFoundException e) {
//			e.printStackTrace();
			return ResponseEntity.status(404).build();
		}
	}
}
