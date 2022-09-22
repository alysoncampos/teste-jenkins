package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.dto.AluguelDTO;
import br.com.dbc.dbcarapi.dto.ClienteDTO;
import br.com.dbc.dbcarapi.entity.ClienteEntity;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fnConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

    public void sendEmailNovoCliente(ClienteDTO clienteDTO, String tipo) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(clienteDTO.getEmail());
            if (tipo.equalsIgnoreCase("create")) {
                mimeMessageHelper.setSubject("Seja bem vindo(a) a DBCar");
            } else if (tipo.equalsIgnoreCase("update")) {
                mimeMessageHelper.setSubject("Seus dados foram atualizados em nosso sistema");
            } else if (tipo.equalsIgnoreCase("delete")) {
                mimeMessageHelper.setSubject("Seus dados foram removidos do nosso sistema!");
            }
            mimeMessageHelper.setText(getContentFromTemplateCliente(clienteDTO, tipo), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplateCliente(ClienteDTO clienteDTO, String tipo) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", clienteDTO.getNome());
        dados.put("id", clienteDTO.getIdCliente());
        dados.put("email", from);

        Template template;
        if (tipo.equalsIgnoreCase("create")) {
            template = fnConfiguration.getTemplate("emailCreateCliente-template.ftl");
        } else if (tipo.equalsIgnoreCase("update")) {
            template = fnConfiguration.getTemplate("emailUpdateCliente-template.ftl");
        } else {
            template = fnConfiguration.getTemplate("emailDeleteCliente-template.ftl");
        }
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }

    public void sendEmailCarroCliente(ClienteEntity clienteEntity, AluguelDTO aluguelDTO, String tipo) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(clienteEntity.getEmail());
            if (tipo.equalsIgnoreCase("alugado")) {
                mimeMessageHelper.setSubject("Carro alugado com sucesso!");
            }
            mimeMessageHelper.setText(getContentFromTemplateAluguel(clienteEntity, aluguelDTO, tipo), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplateAluguel(ClienteEntity clienteEntity, AluguelDTO aluguelDTO, String tipo) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", clienteEntity.getNome());
        dados.put("id", clienteEntity.getIdCliente());
        dados.put("idCarro", aluguelDTO.getCarro().getIdCarro());
        dados.put("email", from);

        Template template = null;
        if (tipo.equalsIgnoreCase("alugado")) {
            template = fnConfiguration.getTemplate("emailAluguelCarro-template.ftl");
        }
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}