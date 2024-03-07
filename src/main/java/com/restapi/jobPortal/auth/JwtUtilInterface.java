package com.restapi.jobPortal.auth;


public interface JwtUtilInterface {
	
	public void setSECRET_KEY(String sECRET_KEY);
	
	public String generateSecretKeyAsString();
	
	public String bytesToHex(byte[] bytes);
	
	public String generateToken(String username);
	
	public boolean validateToken(String token);
	
	public String extractTokenFromHeader(String authorizationHeader);

}
 