package ch.fhnw.scim.scim.service;

import java.util.List;

import ch.fhnw.scim.scim.model.ScimUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.scim.scim.handler.ScimHandler;

import com.unboundid.scim2.common.ScimResource;
import com.unboundid.scim2.common.exceptions.ScimException;

import static com.unboundid.scim2.common.utils.ApiConstants.MEDIA_TYPE_SCIM;

@RestController
@RequestMapping("/scim/v2")
public class ScimService {

	@Autowired
	private ScimHandler handler;

	@GetMapping(produces = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE})
	public List<ScimResource> search() throws ScimException {
		return handler.search();
	}

	@GetMapping(value = "{id}", produces = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE})
	public ScimResource retrieve(@PathVariable("id") String scimId) throws ScimException {
		return handler.retrieve(scimId);
	}

	@PutMapping(value = "{id}", consumes = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE}, produces = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE})
	public ScimResource replace(@PathVariable("id") String scimId, @RequestBody ScimUser scimUser) throws ScimException {
		return handler.replace(scimId, scimUser);
	}

	@PatchMapping(value = "{id}", consumes = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE}, produces = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE})
	public ScimResource modify(@PathVariable("id") String scimId, @RequestBody ScimUser scimUser) throws ScimException {
		return handler.modify(scimId, scimUser);
	}

	@PostMapping(consumes = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE}, produces = {MEDIA_TYPE_SCIM, MediaType.APPLICATION_JSON_VALUE})
	public ScimResource create(@RequestBody ScimUser scimUser) throws ScimException {
		return handler.create(scimUser);
	}

	@DeleteMapping(value = "{id}")
	public void delete(@PathVariable("id") final String id) throws ScimException {
		handler.delete(id);
	}
}
