package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.messaging.MessageService;
import com.matijakljajic.music_catalog.service.messaging.MessageService.ComposeContext;
import com.matijakljajic.music_catalog.service.messaging.MessageService.ConversationChunk;
import com.matijakljajic.music_catalog.service.messaging.MessageService.ConversationView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
@Slf4j
@RequestMapping("/messages")
public class MessagesController {

  private final MessageService messages;

  public MessagesController(MessageService messages) {
    this.messages = messages;
  }

  @GetMapping
  public String chat(@RequestParam(value = "userId", required = false) Long userId,
                     Authentication authentication,
                     Model model) {
    ConversationView view = messages.loadConversation(authentication, userId);
    model.addAttribute("partners", view.getPartners());
    model.addAttribute("activePartner", view.getActivePartner());
    model.addAttribute("chatMessages", view.getMessages());
    model.addAttribute("nextPage", view.getNextPage());
    return "messages/chat";
  }

  @GetMapping("/thread")
  @ResponseBody
  public ConversationChunk loadMore(@RequestParam("userId") Long userId,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    Authentication authentication) {
    return messages.loadMore(authentication, userId, page);
  }

  @GetMapping("/new")
  public String compose(@RequestParam(value = "to", required = false) Long recipientId,
                        Authentication authentication,
                        Model model) {
    ComposeContext context = messages.prepareCompose(authentication, recipientId);
    MessageForm form = new MessageForm();
    if (context.getPreselected() != null) {
      form.setRecipientId(context.getPreselected().getId());
    }
    populateComposeModel(model, context, form);
    return "messages/new";
  }

  @PostMapping
  public String send(@Validated @ModelAttribute("messageForm") MessageForm form,
                     BindingResult bindingResult,
                     Authentication authentication,
                     Model model,
                     RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      ComposeContext context = messages.prepareCompose(authentication, form.getRecipientId());
      populateComposeModel(model, context, form);
      return "messages/new";
    }
    try {
      messages.sendMessage(authentication, form.getRecipientId(), form.getContent());
    } catch (ResponseStatusException ex) {
      log.debug("Unable to send message: {}", ex.getReason());
      if (ex.getStatusCode().is4xxClientError()) {
        String errorMessage = ex.getReason() != null ? ex.getReason() : "Unable to send message.";
        bindingResult.reject("message.error", errorMessage);
        ComposeContext context = messages.prepareCompose(authentication, form.getRecipientId());
        populateComposeModel(model, context, form);
        return "messages/new";
      }
      throw ex;
    }
    redirectAttributes.addFlashAttribute("message", "Message sent.");
    return "redirect:/messages?userId=" + form.getRecipientId();
  }

  @PostMapping("/chat")
  public String sendInConversation(@RequestParam Long recipientId,
                                   @RequestParam String content,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
    try {
      messages.sendMessage(authentication, recipientId, content);
      redirectAttributes.addFlashAttribute("message", "Message sent.");
    } catch (ResponseStatusException ex) {
      String error = ex.getReason() != null ? ex.getReason() : "Unable to send message.";
      redirectAttributes.addFlashAttribute("error", error);
    }
    return "redirect:/messages?userId=" + recipientId;
  }

  private void populateComposeModel(Model model, ComposeContext context, MessageForm form) {
    model.addAttribute("recipientOptions", context.getRecipientOptions());
    model.addAttribute("preselectedRecipient", context.getPreselected());
    model.addAttribute("messageForm", form);
  }

  @Getter
  @Setter
  @Validated
  public static class MessageForm {
    @NotNull(message = "Please choose who to message.")
    private Long recipientId;

    @NotBlank(message = "Message cannot be empty.")
    @Size(max = 2000, message = "Message must be 2000 characters or fewer.")
    private String content;
  }
}
