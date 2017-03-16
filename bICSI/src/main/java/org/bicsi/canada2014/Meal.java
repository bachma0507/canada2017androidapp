package org.bicsi.canada2014;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Meal 
 */

@ParseClassName("WallImageObject")
public class Meal extends ParseObject {

	public Meal() {
		// A default constructor is required.
	}

	public String getTitle() {
		return getString("comment");
	}

	public void setTitle(String comment) {
		put("comment", comment);
	}

	public ParseUser getAuthor() {
		return getParseUser("androiduser");
	}

	public void setAuthor(ParseUser androiduser) {
	//public void setAuthor(String user) {
		put("androiduser", androiduser);
	}
	
	public String getUser() {
		return getString("user");
	}

	public void setUser(String user) {
		put("user", user);
	}

	/*public String getRating() {
		return getString("rating");
	}

	public void setRating(String rating) {
		put("rating", rating);
	}*/

	public ParseFile getPhotoFile() {
		return getParseFile("image");
	}

	public void setPhotoFile(ParseFile file) {
		put("image", file);
	}

}

