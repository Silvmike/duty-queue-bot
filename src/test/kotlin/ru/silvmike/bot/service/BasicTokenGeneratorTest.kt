package ru.silvmike.bot.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BasicTokenGeneratorTest {

    @Test
    fun test() {
        val generator = BasicTokenGenerator()

        Assertions.assertThat(generator.next()).isNotNull()
        Assertions.assertThat(generator.next()).isNotEqualTo(generator.next())
    }
}