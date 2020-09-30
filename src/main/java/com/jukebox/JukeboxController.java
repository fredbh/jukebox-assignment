package com.jukebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jukebox.CustomSettingException;
import com.jukebox.CustomModelException;
import com.jukebox.ErrorInfo;

@RestController
public class JukeboxController {

	private final JukeboxRepository jukeboxRepo;
	
	private final SettingRepository settingRepo;
	
	private static final Logger log = LoggerFactory.getLogger(JukeboxController.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	public JukeboxController(JukeboxRepository jukeboxRepo, SettingRepository settingRepo) {
		this.jukeboxRepo = jukeboxRepo;
		this.settingRepo = settingRepo;
	}

	
	@GetMapping("/jukeboxes")
	public Object[] getValidJukeboxes(@RequestParam(value = "settingId") String settingId, 
			@RequestParam(value = "model", required=false) String modelName,
			@RequestParam(value = "offset", required=false) Integer offset,
			@RequestParam(value = "limit", required=false) Integer limit) {
		
		log.info(settingId);
		ArrayList<Jukebox> validJukeboxes = new ArrayList<Jukebox>();
		
		ArrayList<Jukebox> allJukeboxes = jukeboxRepo.getAllJukeboxes();
		HashMap<String, Setting> allSettings = settingRepo.getAllSettings();
		
		Setting targetSetting = allSettings.get(settingId);
		
		if (targetSetting == null) {
			log.info("setting exception");
			throw new CustomSettingException();
		}
		
		String[] requirements = targetSetting.getRequires();
		
		Jukebox currentJukebox = null;
		Component[] currentComponents = null;
		
		filterModels(allJukeboxes, modelName);
		
		if (modelName != null && allJukeboxes.size() == 0) {
			log.info("model exception");
			throw new CustomModelException();
		}
		
		if (requirements.length > 1) {
		
			for (int i = 0; i < allJukeboxes.size(); i++) {
				currentJukebox = allJukeboxes.get(i);
				currentComponents = currentJukebox.getComponents();
				if (checkComponents(currentComponents, requirements)) {
					validJukeboxes.add(currentJukebox);
				}
			}
			
		} else {
			validJukeboxes = allJukeboxes;
		}
		
		if (offset != null || limit != null) {
			if (offset == null) {
				offset = 0;
			}
			if (limit == null) {
				limit = 0;
			}
			validJukeboxes = new ArrayList<Jukebox>(validJukeboxes.subList(
				    Math.min(validJukeboxes.size(), offset),
				    Math.min(validJukeboxes.size(), offset + limit)));
		}
		
		return validJukeboxes.toArray();
	}
	
	@GetMapping("/clearCache")
	public String clearCache() {
		for(String name:cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();            // clear cache by name
        }
		
		return "Cache cleared.";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({CustomSettingException.class, CustomModelException.class, Exception.class})
	@ResponseBody ErrorInfo
	handleBadRequest(HttpServletRequest req, Exception ex) {
		String errorMessage;
		if (ex instanceof CustomSettingException) {
			errorMessage = "No setting was found for the provided setting id.";
		} else if (ex instanceof CustomModelException){
			errorMessage = "No jukeboxes were found with the provided model name.";
		} else {
			errorMessage = ex.getLocalizedMessage();
		}
	    return new ErrorInfo(req.getRequestURL().toString(), errorMessage);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	@ResponseBody ErrorInfo
	handleBadSettingRequest(HttpServletRequest req, Exception ex) {
		String errorMessage;
		
		ex.getMessage();

		errorMessage = ex.getMessage();
		
	    return new ErrorInfo(req.getRequestURL().toString(), errorMessage);
	}
	
	private boolean checkComponents(Component[] components, String[] requirements){
		boolean allComponents = true;
		int requirementsCount = 0;
		int componentsCount = 0;
		while (requirementsCount < requirements.length && allComponents) {
			boolean componentOk = false;
			componentsCount = 0;
			while (componentsCount < components.length && !componentOk) {
				if(components[componentsCount].compare(requirements[requirementsCount])) {
					componentOk = true;
				}
				componentsCount++;
			}
			if (!componentOk) {
				allComponents = false;
			}
			requirementsCount++;
		}
		
		return allComponents;
	}
	
	private void filterModels(ArrayList<Jukebox> allJukeboxes, String modelName) {
		if (modelName != null) {
			for (int i = 0; i < allJukeboxes.size(); i++) {
				if (!allJukeboxes.get(i).getModel().equals(modelName)) {
					allJukeboxes.remove(i);
					i--;
				}
			}
		}
	}
}