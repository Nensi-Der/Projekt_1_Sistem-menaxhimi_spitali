-- Tabela: doctors
CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialty VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    available BOOLEAN,
    busy_until TIMESTAMP
);

INSERT INTO doctors (name, specialty, email, phone, available, busy_until)
VALUES 
('Arben Hoxha', 'Kardiologji', 'arben.hoxha@hospitalname.com', '+355 67 000 0000', TRUE, NULL),
('Elira Kola', 'Neurologji', 'elira.kola@hospitalname.com', '+355 68 000 0000', TRUE, NULL),
('Gentian Berisha', 'Pediatri', 'gentian.berisha@hospitalname.com', '+355 69 000 0000', TRUE, NULL);

-- Tabela: patients
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE,
    email VARCHAR(255),
    phone VARCHAR(50)
);

-- Tabele e bashkuar patients-doctors per lidhje shume me shume
CREATE TABLE doctor_patients (
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    PRIMARY KEY (doctor_id, patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Tabela: appointments
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    appointment_date TIMESTAMP NOT NULL,
    reason TEXT,
    patient_id INT,
    doctor_id INT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE SET NULL
);

-- Tabela: prescriptions
CREATE TABLE prescriptions (
    id SERIAL PRIMARY KEY,
    appointment_id INT NOT NULL,
    medication VARCHAR(255) NOT NULL,
    dosage VARCHAR(255),
    instructions TEXT,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);

-- Tabela: users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    linked_id BIGINT
);
