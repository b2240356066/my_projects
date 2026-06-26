# Dev-Log Dashboard (My To-Do List) 🌸

**Software Persona 2. Week Project** – Modern, estetik ve işlevsel bir görev yönetim paneli. Bu proje, ReactJS ve Tailwind CSS kullanılarak yapılmıştır.

---

## ✨ Özellikler

Bu uygulama, bir modern web uygulamasında olması gereken temel **CRUD** (Create, Read, Update, Delete) operasyonlarını tam kapsamlı olarak sunar:

* **Ekleme (Create):** Yüksekliği artırılmış, odaklanması kolay devasa bir girdi alanı ile yeni görevler ekleme.
* **Listeleme (Read):** Görevlerin ana ekranda düzenli ve hiyerarşik bir şekilde gösterimi.
* **Güncelleme (Update):** Görevlerin tamamlanma durumunu (Toggle) değiştirme ve görev metnini düzenleme.
* **Silme (Delete):** Gereksiz görevlerin tek tıkla sistemden kaldırılması.

---

## 🛠️ Teknoloji Yığını

Proje, modern frontend geliştirme standartları takip edilerek inşa edilmiştir:

* **[ReactJS](https://reactjs.org/):** Bileşen tabanlı UI geliştirme.
* **[Tailwind CSS](https://tailwindcss.com/):** Utility-first CSS yaklaşımı ile hızlı ve özgün tasarım.
* **[Vite](https://vitejs.dev/):** Yeni nesil hızlı geliştirme ortamı ve build aracı.
* **Lucide Icons / Emoji:** Görsel zenginlik ve kullanıcı deneyimi.

---

## 📂 Proje Yapısı

Ödev yönergesine uygun olarak yapılandırılmış dosya ağacı:

```text
src/
├── Components/    # AddTask.jsx, TaskItem.jsx gibi bileşenler
├── Pages/         # Home.jsx (Ana sayfa logic'i)
├── Interfaces/    # Veri modelleri ve tip tanımlamaları
└── assets/        # Görsel materyaller

 ```

## 🚀 Kurulum ve Çalıştırma

Projeyi kendi bilgisayarınızda çalıştırmak için aşağıdaki adımları takip edebilirsiniz:

### 1. Ön Gereksinimler
Bilgisayarınızda **Node.js** (v14 veya üzeri) yüklü olduğundan emin olun.

### 2. Projeyi İndirin
Öncelikle repo'yu klonlayın veya ZIP olarak indirin:

```bash
git clone https://github.com/b2240356066/SoftwarePersona-WebGelistirmeProjesi.git
   ```

### 3. Bağımlılıkları İndirin
Proje klasörüne gidin ve gerekli kütüphaneleri yüklemek için terminale şu komutu yazın:

```bash
   npm install
   ```
### 4. Uygulamayı Başlatın Geliştirme sunucusunu başlatmak için aşağıdaki komutu kullanın:

```bash
   npm run dev 

```
Uygulama hazır olduğunda terminalde bir link belirecektir (genellikle http://localhost:5173). Bu linke tıklayarak tarayıcınızda görüntüleyebilirsiniz.
