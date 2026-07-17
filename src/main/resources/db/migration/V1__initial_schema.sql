CREATE TABLE doctor (
                        id UUID PRIMARY KEY,

                        full_name VARCHAR(255) NOT NULL,
                        specialty VARCHAR(255) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        email VARCHAR(255) NOT NULL,

                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE patient (
                         id UUID PRIMARY KEY,

                         full_name VARCHAR(255) NOT NULL,
                         document VARCHAR(50) NOT NULL UNIQUE,

                         birth_date DATE NOT NULL,

                         phone VARCHAR(50) NOT NULL,
                         email VARCHAR(255) NOT NULL,

                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL
);

CREATE TABLE reservation (
                             id UUID PRIMARY KEY,

                             doctor_id UUID NOT NULL,
                             patient_id UUID NOT NULL,

                             scheduled_at TIMESTAMP NOT NULL,

                             status VARCHAR(50) NOT NULL,

                             cancelled_at TIMESTAMP NULL,

                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP NOT NULL,

                             CONSTRAINT fk_reservation_doctor
                                 FOREIGN KEY (doctor_id)
                                     REFERENCES doctor(id),

                             CONSTRAINT fk_reservation_patient
                                 FOREIGN KEY (patient_id)
                                     REFERENCES patient(id)
);

CREATE TABLE penalty (
                         id UUID PRIMARY KEY,

                         patient_id UUID NOT NULL,
                         reservation_id UUID NOT NULL,

                         reason VARCHAR(50) NOT NULL,

                         created_at TIMESTAMP NOT NULL,
                         expires_at TIMESTAMP NOT NULL,

                         CONSTRAINT fk_penalty_patient
                             FOREIGN KEY (patient_id)
                                 REFERENCES patient(id),

                         CONSTRAINT fk_penalty_reservation
                             FOREIGN KEY (reservation_id)
                                 REFERENCES reservation(id)
);