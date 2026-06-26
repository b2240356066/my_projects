# Mini Katalog Uygulaması

Bu proje, temel seviyede bir mobil e-ticaret katalog uygulaması taslağıdır. Widget yapısı, named routes ile sayfa geçişleri, model sınıfı oluşturma ve `fromJson` ile dinamik JSON listeleme mantığını içerir.

## Özellikler

* **Discover (Keşfet) Ekranı:** Kart tabanlı ızgara yerleşimi için `GridView.builder` kullanan dinamik bir ürün arayüzü ve üst kısımda bir kampanya banner alanı sunar.
* **Ürün Detay Ekranı:** Sayfalar arasında ürün verilerini taşımak için `Route Arguments` mekanizmasını kullanır. Ürün görselini, açıklamasını ve teknik özelliklerini dinamik olarak ekrana basar.
* **Sepet Simülasyon Sistemi:** Ürün eklendiğinde arayüzdeki sepet sayısını dinamik olarak güncelleyen ve sipariş tamamlama (`Checkout`) akışını simüle eden bir yerel state yönetimi içerir.

## Veri Kaynakları

* **Kampanya Banner Linki:** `https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1000`
* **Örnek Ürün Veri Yapısı:** `https://wantapi.com/products.php`

## Kullanılan Teknolojiler
- **Framework:** Flutter SDK 3.41.9
- **Programlama Dili:** Dart SDK 3.11.5
- **Geliştirme Ortamı (IDE):** Visual Studio Code
- **Çalıştırma Platformu:** Google Chrome (Web Derlemesi)

## Çalıştırma Adımları

1. Bağımlılıkları ve paket bağlantılarını yükleyin:
   ```bash
   flutter pub get

2. Uygulamayı Chrome tarayıcısı üzerinde başlatın:
    ```bash
    flutter run -d chrome

3. Tarayıcı açıldığında mobil görünüm için F12 tuşuna basıp Cihaz Modu (Device Toolbar) ikonuna tıklayın.
    
