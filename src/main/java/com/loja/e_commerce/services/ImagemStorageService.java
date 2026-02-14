package com.loja.e_commerce.services;

import com.loja.e_commerce.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class ImagemStorageService {

    @Value("${storage.upload-dir}")
    private String uploadDir;

    public String salvar(MultipartFile file, String imagemAntiga) {
        validarImagem(file);

        String nomeOriginal = file.getOriginalFilename();
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String novoNome = UUID.randomUUID() + extensao;

        try {
            //Converte a String do appProperties para Path
            Path diretorio = Paths.get(uploadDir);

            // cria o diretório se não existir e não lança erro se já existir
            Files.createDirectories(diretorio);

            // remove imagem antiga se existir
            if (imagemAntiga != null) {
                deletar(imagemAntiga);
                log.warn("Imagem antiga removida: {}", imagemAntiga);
            }

            // Cria novo path diretório + novo nome imagem
            Path caminhoCompleto = diretorio.resolve(novoNome);

            // salva a imagem no disco
            file.transferTo(caminhoCompleto);

            log.info("Imagem salva: {}", novoNome);

            return novoNome;

        } catch (IOException e) {
            log.error("Erro ao salvar a imagem: {}", novoNome, e);
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }

    public void deletar(String nomeArquivo) {
        try {
            Path caminho = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(caminho);
            log.warn("Imagem removida: {}", nomeArquivo);
        } catch (IOException e) {
            log.error("Erro ao deletar a imagem: {}", nomeArquivo, e);
            throw new RuntimeException("Erro ao deletar imagem", e);
        }
    }

    private void validarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            throw new BadRequestException("Arquivo de imagem é obrigatório");
        }

        String contentType = imagem.getContentType();

        //null -> o metadado não é confiável
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new BadRequestException("Tipo de arquivo inválido. Apenas JPG ou PNG são permitidos");
        }

        long tamanhoMaximo = 2 * 1024 * 1024; // 2MB

        if (imagem.getSize() > tamanhoMaximo) {
            throw new BadRequestException("Imagem excede o tamanho máximo de 2MB");
        }
    }
}
