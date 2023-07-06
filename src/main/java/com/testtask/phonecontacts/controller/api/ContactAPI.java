package com.testtask.phonecontacts.controller.api;

import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Contacts", description = "Operations related to contacts")
@SecurityRequirement(name = "Token")
@RequestMapping("/contacts")
public interface ContactAPI {

    @Operation(summary = "Get all user contacts",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ContactModel.class))))
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ContactModel> getContacts(@AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "Create contact",
            responses = @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ContactModel.class))))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ContactModel createContact(@Valid @RequestBody ContactModel contactModel,
                               @AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "Edit contact",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ContactModel.class))))
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ContactModel editContact(@Valid @RequestBody ContactModel contactModel,
                             @AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "Delete contact",
            responses = @ApiResponse(responseCode = "204",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))))
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteContact(@RequestParam String name, @AuthenticationPrincipal UserPrincipal userPrincipal);
}
