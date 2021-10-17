package com.ajo.itedo.data;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AjoBranchTest {
    AjoBranch ajoBranch;
    @BeforeEach
    void setUp() {
        ajoBranch = new AjoBranch();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createBranch(){
        ajoBranch.setBranchName("Shomolu");
        Assertions.assertEquals(ajoBranch.getBranchName(),"Shomolu");
    }
}