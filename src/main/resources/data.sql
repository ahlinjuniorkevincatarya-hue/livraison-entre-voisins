-- =====================================================================
-- Données de test : Livraison entre voisins
-- Mot de passe de tous les comptes : password123 (hash BCrypt)
-- =====================================================================

-- ---------- QUARTIERS ----------
INSERT INTO quartier (id, nom, ville) VALUES (1, 'Maarif', 'Casablanca');
INSERT INTO quartier (id, nom, ville) VALUES (2, 'Gauthier', 'Casablanca');

-- ---------- UTILISATEURS (table mère) ----------
-- 1 admin, 2 à 6 voisins, 7 et 8 agences
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (1, 'Admin',   'admin@livraison.ma',   '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'ADMIN',  true);
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (2, 'Amine',   'amine@mail.ma',        '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'VOISIN', true);
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (3, 'Sara',    'sara@mail.ma',         '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'VOISIN', true);
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (4, 'Youssef', 'youssef@mail.ma',      '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'VOISIN', true);
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (5, 'Lina',    'lina@mail.ma',         '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'VOISIN', true);
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (6, 'Omar',    'omar@mail.ma',         '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'VOISIN', false); -- compte bloqué (RG12)
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (7, 'Express', 'express@agence.ma',    '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'AGENCE', true);
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role, actif) VALUES (8, 'Rapido',  'rapido@agence.ma',     '$2a$10$3P7vKdlUrFt1WXTOMXyRXOBnAe3tIRRC4vJL733lJ0JT5l7aGUlq6', 'AGENCE', true);

-- ---------- VOISINS (même id que dans la table utilisateur) ----------
INSERT INTO voisin (id, adresse, quartier_id) VALUES (2, '12 rue Mers Sultan',   1);
INSERT INTO voisin (id, adresse, quartier_id) VALUES (3, '8 rue d''Alger',       1);
INSERT INTO voisin (id, adresse, quartier_id) VALUES (4, '25 bd Zerktouni',      1);
INSERT INTO voisin (id, adresse, quartier_id) VALUES (5, '3 rue Gauthier',       2);
INSERT INTO voisin (id, adresse, quartier_id) VALUES (6, '17 rue Gauthier',      2);

-- ---------- AGENCES (même id que dans la table utilisateur) ----------
INSERT INTO agence_livraison (id, nom_agence, valide) VALUES (7, 'Express Livraison', true);
INSERT INTO agence_livraison (id, nom_agence, valide) VALUES (8, 'Rapido Express',    false); -- à valider par l'admin (F14)

-- Quartiers desservis
INSERT INTO agence_quartier (agence_id, quartier_id) VALUES (7, 1);
INSERT INTO agence_quartier (agence_id, quartier_id) VALUES (7, 2);
INSERT INTO agence_quartier (agence_id, quartier_id) VALUES (8, 1);

-- ---------- PRODUITS ----------
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (1, 'Lait 1L',            5.0,  true,  7);
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (2, 'Pain',               2.0,  true,  7);
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (3, 'Oeufs (boîte de 12)', 15.0, true,  7);
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (4, 'Riz 1kg',            12.0, true,  7);
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (5, 'Huile d''olive 1L',  55.0, true,  7);
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (6, 'Fromage',            18.0, false, 7); -- produit désactivé (RG5)
INSERT INTO produit (id, nom, prix, actif, agence_id) VALUES (7, 'Thé 200g',           20.0, true,  8);

-- ---------- PAIEMENT (avant participation, à cause de la clé étrangère) ----------
INSERT INTO paiement (id, montant, date_paiement) VALUES (1, 67.0, CURRENT_DATE);

-- ---------- COMMANDES GROUPÉES ----------
-- G1 : OUVERTE, organisateur Amine (quartier Maarif), limite dans 1 jour
INSERT INTO commande_groupee (id, statut, heure_limite, point_retrait, frais_livraison, date_creation, agence_id, organisateur_id)
VALUES (1, 'OUVERTE',   DATEADD('DAY', 1, CURRENT_TIMESTAMP),   'Devant l''immeuble, rue Mers Sultan', NULL, CURRENT_TIMESTAMP, 7, 2);
-- G2 : CLOTUREE (limite dépassée), organisateur Youssef, prête pour confirmation par l'agence (F11)
INSERT INTO commande_groupee (id, statut, heure_limite, point_retrait, frais_livraison, date_creation, agence_id, organisateur_id)
VALUES (2, 'CLOTUREE',  DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 'Hall du 25 bd Zerktouni',            NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 7, 4);
-- G3 : CONFIRMEE, frais 30 DH, 3 participants -> pour tester le calcul des parts (F12)
INSERT INTO commande_groupee (id, statut, heure_limite, point_retrait, frais_livraison, date_creation, agence_id, organisateur_id)
VALUES (3, 'CONFIRMEE', DATEADD('DAY', -1, CURRENT_TIMESTAMP),  'Devant l''immeuble, rue Mers Sultan', 30.0, DATEADD('DAY', -2, CURRENT_TIMESTAMP), 7, 2);
-- G4 : LIVREE (quartier Gauthier), organisatrice Lina, Lina a déjà réglé, Omar non (F13)
INSERT INTO commande_groupee (id, statut, heure_limite, point_retrait, frais_livraison, date_creation, agence_id, organisateur_id)
VALUES (4, 'LIVREE',    DATEADD('DAY', -2, CURRENT_TIMESTAMP),  '3 rue Gauthier',                     20.0, DATEADD('DAY', -3, CURRENT_TIMESTAMP), 7, 5);

-- ---------- PARTICIPATIONS ----------
-- G1
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (1, false, '12 rue Mers Sultan', 1, 2, NULL);
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (2, false, NULL,                 1, 3, NULL);
-- G2
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (3, false, '25 bd Zerktouni',     2, 4, NULL);
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (4, false, '8 rue d''Alger',     2, 3, NULL);
-- G3
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (5, false, '12 rue Mers Sultan', 3, 2, NULL);
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (6, false, '8 rue d''Alger',     3, 3, NULL);
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (7, false, '25 bd Zerktouni',     3, 4, NULL);
-- G4
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (8, true,  '3 rue Gauthier',      4, 5, 1);
INSERT INTO participation (id, est_regle, lieu_livraison, commande_id, voisin_id, paiement_id) VALUES (9, false, '17 rue Gauthier',     4, 6, NULL);

-- ---------- LIGNES D'ARTICLES ----------
-- G1
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (1,  2,  5.0, 1, 1);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (2,  1, 15.0, 1, 3);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (3,  4,  2.0, 2, 2);
-- G2
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (4,  1, 12.0, 3, 4);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (5,  3,  5.0, 3, 1);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (6,  2,  2.0, 4, 2);
-- G3 : totaux articles = Amine 31, Sara 22, Youssef 39 -> avec frais 30/3 = 10 : parts 41, 32, 49
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (7,  2,  5.0, 5, 1);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (8,  3,  2.0, 5, 2);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (9,  1, 15.0, 5, 3);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (10, 1, 12.0, 6, 4);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (11, 2,  5.0, 6, 1);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (12, 2, 12.0, 7, 4);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (13, 1, 15.0, 7, 3);
-- G4 : articles Lina 57, Omar 22 -> avec frais 20/2 = 10 : parts 67 et 32
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (14, 1, 55.0, 8, 5);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (15, 1,  2.0, 8, 2);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (16, 2,  5.0, 9, 1);
INSERT INTO ligne_article (id, quantite, prix_unitaire, participation_id, produit_id) VALUES (17, 1, 12.0, 9, 4);

-- ---------- Les nouveaux ids générés par l'application repartent après les données de test ----------
ALTER TABLE quartier          ALTER COLUMN id RESTART WITH 100;
ALTER TABLE utilisateur       ALTER COLUMN id RESTART WITH 100;
ALTER TABLE produit           ALTER COLUMN id RESTART WITH 100;
ALTER TABLE paiement          ALTER COLUMN id RESTART WITH 100;
ALTER TABLE commande_groupee  ALTER COLUMN id RESTART WITH 100;
ALTER TABLE participation     ALTER COLUMN id RESTART WITH 100;
ALTER TABLE ligne_article     ALTER COLUMN id RESTART WITH 100;