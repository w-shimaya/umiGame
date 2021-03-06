package com.inorista.situationpuzzle.domain;

import discord4j.core.object.reaction.ReactionEmoji;

/**
 * Reaction emojis to process.
 */
public enum AvailableEmoji {
  CROSS("U+274C"), CIRCLE("U+2B55"), PROHIBITED("U+1F6AB");

  private final ReactionEmoji.Unicode emoji;

  AvailableEmoji(String codepoint) {
    emoji = ReactionEmoji.Unicode.codepoints(codepoint);
  }

  public String getRaw() {
    return emoji.getRaw();
  }

  public ReactionEmoji.Unicode getReactionEmoji() {
    return emoji;
  }

  /**
   * Get enum by raw string.
   *
   * @param raw Raw string
   * @return Enum
   */
  public static AvailableEmoji getByRaw(String raw) {
    for (AvailableEmoji emoji : AvailableEmoji.values()) {
      if (emoji.getRaw().equals(raw)) {
        return emoji;
      }
    }
    return null;
  }
}
