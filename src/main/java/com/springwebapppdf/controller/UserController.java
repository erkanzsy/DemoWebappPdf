package com.springwebapppdf.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.springwebapppdf.model.User;
import com.springwebapppdf.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("users",userRepository.findAll());
        return "home.html";
    }

    @PostMapping("/adduser")
    public String userEkle(@ModelAttribute User s_user,Model model){
        userRepository.save(s_user);
        model.addAttribute("message",s_user.getName() + " adlı user eklendi");
        model.addAttribute("user", new User());
        model.addAttribute("users",userRepository.findAll());
        return "home.html";
    }

    @RequestMapping("/edit/{id}")
    public String editUser(@PathVariable("id")  int id, Model model){
        model.addAttribute("user", userRepository.getOne(id));
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("message",userRepository.getOne(id).getName() + " adlı user düzenlenmek için seçildi");
        userRepository.deleteById(id);
        return "home.html";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id")  int id, Model model){
        User user = userRepository.getOne(id);
        userRepository.deleteById(id);
        model.addAttribute("message",user.getName() + " adlı user silindi");
        model.addAttribute("user", new User());
        model.addAttribute("users",userRepository.findAll());
        return "home.html";
    }

    @RequestMapping(value = "/download.pdf",method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<InputStreamResource> pdf() throws Exception{
        System.out.println("Pdf al");

        List<User> users = userRepository.findAll();

        ByteArrayInputStream byteArrayInputStream = userPdf(users);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Contoent-Disposition","inline=users.pdf");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4,16,16,32,16);
        PdfWriter.getInstance(document,bos);

        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }

    private ByteArrayInputStream userPdf(List<User> users) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Font headfont = FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.WHITE);
            List<String> headers = new ArrayList<>();
            headers.add("No");
            headers.add("Ad");
            headers.add("Soyad");
            headers.add("Rol");

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(90);
            table.setWidths(new int[]{1,3,3,2});

            for (String string : headers){
                PdfPCell cell;
                cell = new PdfPCell(new Phrase(string,headfont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                table.addCell(cell);
            }


            Font font = FontFactory.getFont(FontFactory.COURIER, 8);

            BaseColor lgray = BaseColor.LIGHT_GRAY;
            BaseColor gray = BaseColor.WHITE;


            int count = 0;

            for (User user : users) {
                count++;

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(String.valueOf(count), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(user.getName(), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(user.getSurname()), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(user.getRole()), font));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(0 == (count%2) ? lgray:gray);
                table.addCell(cell);
            }

            LocalDateTime myDateObj = LocalDateTime.now();

            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String formattedDate = myDateObj.format(myFormatObj);

            PdfWriter.getInstance(document,out);
            document.open();
            Paragraph para = new Paragraph();
            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
            Font font1 = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);

            para = new Paragraph("X Company's Users", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            para = new Paragraph(" ", fontHeader);
            document.add(para);

            para = new Paragraph(formattedDate, font);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            para = new Paragraph(" ", font);
            document.add(para);

            document.add(table);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


}
