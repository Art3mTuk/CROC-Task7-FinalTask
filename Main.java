package CROC.finalTask;

import javax.xml.bind.annotation.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Финальное задание.
 * Для первого задание вариант - 3
 * Для второго задания вариант - 0
 * Для формата файлов вариант - 1
 *
 * @author Artem Tukalenko
 */
@XmlRootElement
class Product {
    @XmlElement
    public int productId;

    @XmlElement
    public String productName;
}

@XmlRootElement
class Seller {
    @XmlElement
    public int sellerId;

    @XmlElement
    public String lastName;

    @XmlElement
    public String firstName;
}

@XmlRootElement
class Inventory {
    @XmlElement
    public int sellerId;

    @XmlElement
    public int productId;

    @XmlElement
    public double price;

    @XmlElement
    public int quantity;
}

@XmlRootElement
class Sale {
    @XmlElement
    public int saleId;

    @XmlElement
    public int sellerId;

    @XmlElement
    public int productId;

    @XmlElement
    public int quantity;

    @XmlElement
    public String saleDate;
}

public class Main {
    public static void main(String[] args) {
        try {
            // Чтение данных из XML файлов
            List<Product> products = readXML("products.xml", Product.class);
            List<Seller> sellers = readXML("sellers.xml", Seller.class);
            List<Inventory> inventories = readXML("inventories.xml", Inventory.class);
            List<Sale> sales = readXML("sales.xml", Sale.class);

            // Задание 1: Для каждого товара вывести в файл общее количество проданных товаров этого типа
            Map<Integer, Integer> productSoldCount = new HashMap<>();
            for (Sale sale : sales) {
                int productId = sale.productId;
                int quantity = sale.quantity;
                productSoldCount.put(productId, productSoldCount.getOrDefault(productId, 0) + quantity);
            }
            writeJSON("product_sold_count.json", productSoldCount);

            // Задание 2: Вывести в файл распределение общего количества продаж по датам
            Map<String, Integer> salesByDate = new HashMap<>();
            for (Sale sale : sales) {
                String saleDate = sale.saleDate;
                int quantity = sale.quantity;
                salesByDate.put(saleDate, salesByDate.getOrDefault(saleDate, 0) + quantity);
            }
            writeJSON("sales_by_date.json", salesByDate);

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> List<T> readXML(String filename, Class<T> clazz) throws JAXBException {
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return ((List<T>) jaxbUnmarshaller.unmarshal(file));
    }

    private static void writeJSON(String filename, Object data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileWriter writer = new FileWriter(filename);
        mapper.writeValue(writer, data);
        writer.close();
    }
}
